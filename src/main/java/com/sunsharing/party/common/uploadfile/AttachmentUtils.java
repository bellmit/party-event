package com.sunsharing.party.common.uploadfile;

import com.sunsharing.component.utils.base.DateUtils;
import com.sunsharing.party.common.dfs.FileFactory;

import org.apache.log4j.Logger;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;

/**
 * Created by criss on 14-1-2.
 */
public class AttachmentUtils {
	static Logger logger = Logger.getLogger(AttachmentUtils.class);
    /**
     * 以filename名保存文件
     * @param image
     * @param relativeName 注意saveFile返回的相对路径
     * @return
     */
    public static String saveFileWithFileName(BufferedImage image,String relativeName, String tmpPath){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            int t = relativeName.indexOf(".");
            String ext = relativeName.substring(t+1);
            ImageIO.write(image, ext, out);
            byte[] bytes = out.toByteArray();
            saveFileWithFileName(bytes,relativeName,tmpPath);
        }catch (IOException e){
            logger.error("输出文件失败",e);
            throw new RuntimeException("保存失败");
        }finally {
            if(out!=null)
            {
                try
                {
                    out.close();
                }catch (Exception e)
                {

                }
            }
        }
        return relativeName;
    }
    /**
     * 以filename名保存文件
     * @param content
     * @param relativeName 注意saveFile返回的相对路径
     * @return
     */
    public static String saveFileWithFileName(byte[] content,String relativeName, String tmpPath){
       // Map result = getFileByFileName(relativeName,"");
        FileOutputStream out = null;
        String fileByFileName = getFileByFileName(relativeName,tmpPath);
        try
        {
            out = new FileOutputStream(new File(fileByFileName));
            out.write(content);
            out.flush();
        }catch (Exception e)
        {
            logger.error("输出文件失败",e);
            throw new RuntimeException("保存失败");
        }finally {
            if(out!=null)
            {
                try
                {
                    out.close();
                }catch (Exception e)
                {

                }
            }
        }
         String path = FileFactory.create().saveFile(new File(fileByFileName));
      //  return path;
		return relativeName;
    }
    
    
    
    /**
     * 取得文件参数，当relativeName为空时，根据自定义规则生成文件名
     * @param relativeName
     * @param format
     * @return
     */
    public static String FILE_SEPARATOR  = "/";
    private static String getFileByFileName(String relativeName,String attPath)
    {
    	  String dt = DateUtils.transFormat(new Date(), "yyyyMMdd");
    	  attPath += dt;
    	  File dir = new File(attPath);
          if(!dir.exists())
          {
              dir.mkdirs();
          }
          int t = relativeName.lastIndexOf(FILE_SEPARATOR);
          String ext = relativeName.substring(t+1);
          String tempDir =  attPath+FILE_SEPARATOR+ext;
          return tempDir;
    }
    
}
