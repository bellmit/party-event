package com.sunsharing.party.common.dfs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.DateUtils;
import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.party.ConfigParam;
import com.sunsharing.party.common.uploadfile.AttachmentUtils;
import com.sunsharing.party.common.uploadfile.ImageUtils;
import com.sunsharing.party.util.FTPUtils;

public class Uploader {
	
	Logger logger = Logger.getLogger(Uploader.class);
	private HttpServletRequest request = null;
	private final static String baseDir = "/static/";
	private List<String> allowFilesList = new ArrayList<String>() {
		
		{
			add(".jpg");
			add(".png");
			add(".jpeg");
			add(".gif");
			add(".bmp");
			add(".txt");
			add(".doc");
			add(".docx");
			add(".xls");
			add(".xlsx");
			add(".ppt");
			add(".pptx");
			add(".pdf");
			add(".xml");
			add(".rar");
			add(".zip");
			add(".7z");
			add(".gz");
			add(".bz");
			add(".ace");
			add(".uha");
			add(".uda");
			add(".zpaq");
		}
	};
	// 最大文件大小，单位byte
	private int thresholdSize = 1 * 1024 * 1024;
	private int fileMaxSize = 5 * 1024 * 1024;
	private int allMaxSize = 20 * 1024 * 1024;
	
	public Uploader(HttpServletRequest request) {
		this.request = request;
	}
	
	public List<String> getAllowFilesList() {
		return allowFilesList;
	}
	
	public void setAllowFilesList(List<String> allowFilesList) {
		this.allowFilesList = allowFilesList;
	}
	
	public int getThresholdSize() {
		return thresholdSize;
	}
	
	public void setThresholdSize(int thresholdSize) {
		this.thresholdSize = thresholdSize;
	}
	
	public int getFileMaxSize() {
		return fileMaxSize;
	}
	
	public void setFileMaxSize(int fileMaxSize) {
		this.fileMaxSize = fileMaxSize;
	}
	
	public int getAllMaxSize() {
		return allMaxSize;
	}
	
	public void setAllMaxSize(int allMaxSize) {
		this.allMaxSize = allMaxSize;
	}
	
	/**
	 * 执行方法
	 * @param map
	 */
	private void execute(Map map) {
		map.put("status", false);
		final List data = new ArrayList();
		map.put("data", data);
		String tmpPath = (String)map.get("tmpPath");
		tmpPath = tmpPath.replace("\\", "/");
		if (!tmpPath.endsWith("/")) {
			tmpPath = tmpPath + "/";
		}
		if (tmpPath.startsWith("/")) {
			tmpPath = tmpPath.substring(1, tmpPath.length());
		}
		final File tmpDir = new File(tmpPath); // 初始化上传文件的临时存放目录
		try {
			if (!tmpDir.isDirectory()) {
				tmpDir.mkdirs(); // 缓存目录没有则创建
			}
			
			// 设置参数
			setSystemParam(tmpDir);
			
			// 传入Map<String, MultipartFile> items 对象进行操作
			if (map.containsKey("items")) {
				final Map<String, MultipartFile> items = (Map<String, MultipartFile>)map.get("items");
				if (items != null) {
					Map<String, Object> objMap = null;
					for (final String fieldName : items.keySet()) {
						final MultipartFile item = items.get(fieldName);
						objMap = new HashMap<String, Object>();
						objMap.put("item", item);
						objMap.put("imageSize", map.get("imageSize"));
						saveFile(data, tmpPath, fieldName, item.getOriginalFilename(), objMap);
					}
				}
			}
			// else if(map.containsKey("picList"))//传入List<InPic> picList 对象进行操作
			// {
			// List<InPic> picList = (List<InPic>)map.get("picList");
			// if(picList!=null)
			// {
			// Map<String, Object> objMap = null;
			// for (InPic pic : picList) {
			// objMap = new HashMap<String, Object>();
			// objMap.put("pic", pic);
			// objMap.put("imageSize", map.get("imageSize"));
			// saveFile(data, tmpPath, pic.getPicName(), pic.getPicName(),objMap);
			// }
			// }
			// }
			// 统一的结果返回
			if (data.size() > 0) {
				map.put("status", true);
				map.put("msg", "保存成功");
				map.put("code", "Success");
			} else {
				map.put("msg", "读取不到上传的文件！");
				map.put("code", "NoFound");
			}
		} catch (final IllegalStateException e) {
			logger.error("保存文件不允许的文件类型", e);
			map.put("msg", "不允许的文件类型");
			map.put("code", "TypeErr");
		} catch (final Exception e) {
			logger.error("保存文件异常", e);
			map.put("msg", "内部异常");
			map.put("code", "Err");
		}
	}
	
	/**
	 * 保存文件
	 * @param data 迭代返回数据
	 * @param tmpPath 临时路径地址
	 * @param fieldName 字段
	 * @param originFileName 源文件名
	 * @param objMap 传入的对象参数，需要根据该对象进行一定的操作
	 * @throws Exception
	 */
	private void saveFile(List data, String tmpPath, String fieldName, String originFileName, Map<String, Object> objMap)
	        throws Exception {
		if (originFileName.length() > 0) {
			final String suffix = originFileName.substring(originFileName.lastIndexOf(".")).toLowerCase();// 获得上传文件的文件名
			if (!allowFilesList.contains(suffix)) {
				// 不允许的文件名
				throw new IllegalStateException("不允许的文件类型！");
			}
			String type = "image";
			if (".png,.jpg,.jpeg,.gif,.bmp".indexOf(suffix) == -1) {
				type = "file";
			}
			final String physicalPath = getPhysicalPath(tmpPath, type, suffix);
			logger.info("取得文件名：" + physicalPath);
			if (objMap.containsKey("pic")) {
				// Base64ToImg.GenerateImage(((InPic)objMap.get("pic")).getPicContent(), physicalPath);
			}
			final File file = new File(physicalPath);
			byte[] multbyte = null;
			if (objMap.containsKey("item")) {
				final MultipartFile multfile = ((MultipartFile)objMap.get("item"));
				multbyte = multfile.getBytes();
				multfile.transferTo(file);
			}
			String relativeName = FileFactory.create().saveFile(file);
			
			// 新增图片保存的时候 如果有指定图片大小参数，就多生成一张指定大小的缩略图
			final String imageSize = (String)objMap.get("imageSize");
			if (!StringUtils.isBlank(imageSize)) {
				extSaveScleImage(tmpPath, relativeName, imageSize, multbyte);
			}
			
			relativeName = baseDir + type + relativeName;
			logger.info("最终保存后文件相对名：" + relativeName);
			final Map storageState = new HashMap();
			storageState.put("url", relativeName.replace("\\", "/"));
			storageState.put("type", suffix);
			storageState.put("original", originFileName);
			storageState.put("fieldName", fieldName);
			data.add(storageState);
		}
	}
	
	/**
	 * 保存图片自定义比例压缩后的图片
	 * @param tmpPath
	 * @param relativeName
	 * @param imageSize
	 * @throws IOException
	 */
	private void extSaveScleImage(String tmpPath, String relativeName, String imageSize, byte[] multbyte) throws IOException {
		// 读
		byte[] readFile = multbyte;
		if (multbyte == null || multbyte.length == 0) {
			readFile = FileFactory.create().readFile(relativeName);
		}
		final String imageName = ImageUtils.getImageName(imageSize, relativeName);
		final int t = imageSize.indexOf("x");
		final int width = Integer.valueOf(imageSize.substring(0, t - 1));
		final int height = Integer.valueOf(imageSize.substring(t + 1));
		// 转
		final BufferedImage scaleImage = ImageUtils.scaleImage(readFile, width, height);
		// 存
		AttachmentUtils.saveFileWithFileName(scaleImage, imageName, tmpPath);
	}
	
	public static void main(String[] args) throws Exception {
		final String filePath = "C:\\Users\\asus\\Pictures\\实例图片\\example_qzgx_cszm.jpg";
		// 读
		byte[] buffer = null;
		try {
			final File file = new File(filePath);
			final FileInputStream fis = new FileInputStream(file);
			final ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
			final byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (final IOException e) {
			
		}
		// 转
		final BufferedImage scaleImage = ImageUtils.scaleImage(buffer, 600, 600);
		
		// 保存
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		ImageIO.write(scaleImage, "jpg", out);
		final byte[] bytes = out.toByteArray();
		
		FileOutputStream out2 = null;
		out2 = new FileOutputStream(new File("C:\\Users\\asus\\Pictures\\实例图片\\example_qzgx_cszm600600.jpg"));
		out2.write(bytes);
		out2.flush();
	}
	
	/**
	 * 执行保存
	 * @return {"status":true,"code":"000","msg":"保存成功","data":[{"url":"/20140608/030501238456.jpg","type":".jpg","original":"测试.jpg","fieldName":"file1"}]}
	 */
	public Map doExec() {
		// map单独提出来，方便每个方法自己设置自己的特色
		final Map map = new HashMap();
		
		Map<String, MultipartFile> items = null;
		if (ServletFileUpload.isMultipartContent(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = null;
			if (request instanceof MultipartHttpServletRequest) {
				multipartHttpServletRequest = (MultipartHttpServletRequest)request;
			} else {
				multipartHttpServletRequest = new CommonsMultipartResolver().resolveMultipart(request);
			}
			items = multipartHttpServletRequest.getFileMap();
			
		}
		// 该方法是由web端调用
		final String tmpPath = request.getSession().getServletContext().getRealPath(baseDir);
		map.put("items", items);
		map.put("tmpPath", tmpPath);
		map.put("imageSize", request.getParameter("imageSize"));
		execute(map);
		map.remove("items");
		map.remove("tmpPath");
		map.remove("imageSize");
		return map;
	}
	
	/**
	 * 执行保存
	 * @return {"status":true,"code":"000","msg":"保存成功","data":[{"url":"/20140608/030501238456.jpg","type":".jpg","original":"测试.jpg","fieldName":"file1"}]}
	 */
	/*
	 * public Map doExec(List<InPic> picList){ //map单独提出来，方便每个方法自己设置自己的特色 Map map = new HashMap(); //由于该方法是由air-server调用，直接从当前类获取根目录 String tmpPath =
	 * this.getClass().getClassLoader().getResource("/").getPath()+baseDir; map.put("tmpPath", tmpPath); map.put("picList", picList); execute(map);
	 * map.remove("picList"); map.remove("tmpPath"); return map; }
	 */
	/**
	 * 设置系统参数
	 * @param tmpDir
	 */
	private void setSystemParam(File tmpDir) {
		final DiskFileItemFactory factory = new DiskFileItemFactory();
		// 指定在内存中缓存数据大小,单位为byte
		factory.setSizeThreshold(thresholdSize);
		// 设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录
		factory.setRepository(tmpDir);
		final ServletFileUpload sfu = new ServletFileUpload(factory);
		// 指定单个上传文件的最大尺寸,单位:字节
		sfu.setFileSizeMax(fileMaxSize);
		// 指定一次上传多个文件的总尺寸,单位:字节
		sfu.setSizeMax(allMaxSize);
		// 设置编码
		sfu.setHeaderEncoding("UTF-8");
	}
	
	/**
	 * 取得文件名（时间戳）{yyyy}{mm}{dd}/{time}{rand:6}
	 * @param tmpPath
	 * @param type
	 * @param suffix
	 * @return
	 */
	private String getPhysicalPath(String tmpPath, String type, String suffix) {
		final String now = DateUtils.transFormat(new Date(), "yyyyMMddHHmmss");
		final String path = tmpPath + type + "/" + now.substring(0, 8);
		if (!new File(path).isDirectory()) {
			new File(path).mkdirs();
		}
		final StringBuilder sb = new StringBuilder(path);
		sb.append("/");
		sb.append(System.currentTimeMillis());
		sb.append((Math.random() + "").replace(".", "").substring(0, 6));
		sb.append(suffix);
		return sb.toString();
	}
	/**
     * 文件保存   
     * @param fileElementId   文件选择框的id属性
     * @param saveDir   文件保存的绝对路径   code 文件保存的的文件名前缀
     * @param useSelfName 使用自身名称
     * @param useFTP 使用FTP上传
     * @param request
     * @return JSONObject
     */
    public static JSONObject uploadFiles(String code , String fileElementId, String saveDir,boolean useSelfName,boolean useFTP,HttpServletRequest request){
    	 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;  
    	 JSONObject resultObj = new JSONObject();
//         if(!useFTP)
//        	 saveDir = request.getSession().getServletContext().getRealPath(saveDir);
         /**得到图片保存目录的真实路径**/
         String logoRealPathDir = saveDir;//  String saveDir = "D:\\test";
         /**根据真实路径创建目录**/
         File logoSaveFile = new File(logoRealPathDir);       
         if(!logoSaveFile.exists()){       
             logoSaveFile.mkdirs(); 
             }            
        
         /**页面控件的文件流**/
         MultipartFile multipartFile = multipartRequest.getFile(fileElementId);   
       
         /**获取文件的后缀**/
         String filenameString  = multipartFile.getOriginalFilename();
//         System.out.println(filenameString);  
         String suffix = multipartFile.getOriginalFilename().substring    
         (multipartFile.getOriginalFilename().lastIndexOf("."));   
         
         /**拼成完整的文件保存路径加文件**/
        //String name = +  System.currentTimeMillis()+"---"+filenameString;
         String name = code.replace("data", "jjyx") +"_"+ DateUtils.getDBString(new Date())+suffix;
         name = (useSelfName?filenameString:name);
         String fileName = logoRealPathDir + File.separator+name;      
         File file = new File(fileName);   
//         String data = file.getPath();  
         
         try {  
             resultObj.put("flag", "success");
             resultObj.put("newFileName", name);
             
             if(!useFTP){
            	 if("yjxx".equals(code)){
            		 resultObj.put("newFileName",FileFactory.create().saveFile(file));
            	 }else
            		 multipartFile.transferTo(file);
             }
             else{
	        	FTPUtils ftp = new FTPUtils();
				ftp.connect(ConfigParam.ftpPath+saveDir, ConfigParam.ftpIpPort.split(":")[0]
						 , Integer.valueOf(ConfigParam.ftpIpPort.split(":")[1]),
						 ConfigParam.ftpUser, ConfigParam.ftpPwd);
//				ftp.upload(file);
				ftp.upload(file.getName(), multipartFile.getInputStream());
             }
         } catch (Exception e) {  
             e.printStackTrace();  
             resultObj.put("flag", "上传出错啦,文件路径不对");
//         } catch (IOException e) {  
//             e.printStackTrace();  
//             resultObj.put("flag", "上传出错啦，文件路径不对");
         }
         return resultObj;
    }
}
