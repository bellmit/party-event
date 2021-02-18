package com.sunsharing.party.web;


import com.sunsharing.component.utils.base.StringUtils;
import com.sunsharing.party.common.dfs.FileFactory;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Controller
public class CommonController{
    Logger logger = Logger.getLogger(CommonController.class);

    @RequestMapping(value={"/attachment"})
    public void getFile(HttpServletResponse response,String filename) throws Exception {
       logger.info("filename:"+filename);
        if(!StringUtils.isBlank(filename)){
            if(filename.toLowerCase().endsWith(".jpg"))//使用编码处理文件流的情况：
            {
                response.setContentType("image/jpg;charset=utf-8");//设定输出的类型
            }else if(filename.toLowerCase().endsWith(".png"))
            {
                response.setContentType("image/png;charset=utf-8");//设定输出的类型
            }else{
                response.setContentType("application/octet-stream");//设定输出的类型
                response.setHeader("Content-Disposition", "attachment;"
                        + " filename="+new String(filename.getBytes("UTF-8"), "ISO8859-1"));
            }
            OutputStream os = response.getOutputStream();
            try {
            	if(!filename.startsWith("/"))
            	{
            		filename = "/"+filename;
            	}
    			byte[] arr = FileFactory.create().readFile(filename);
    			try {
    				os.write(arr);
    				// ImageUtiles.resizeImage("360", "360", relativeName.substring(relativeName.lastIndexOf(".")), outputStream, arr);
    			} catch (Exception e) {
    				logger.error("获取图片出错:", e);
    				// outputStream.write(arr);
    			} finally {
    				os.flush();
    				os.close();
    			}
    		} catch (Exception e) {
    			logger.error("输出失败", e);
    			throw new RuntimeException("输出失败");
    		}
        }
    }

}
