package com.sunsharing.party.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class ImageUtils {
	/**
	 * 等比例压缩图片
	 * @param width
	 * @param height
	 * @param suffix
	 * @param os
	 * @param bytes
	 * @throws IOException
	 */
	public static void resizeImage(String width, String height, String suffix,
			OutputStream os, byte[] bytes) throws IOException {
		if(width!=null && height!=null){
//                if(picContentType.get(filenameTail)!=null){
			Integer w = Integer.parseInt(width);
		    Integer h = Integer.parseInt(height);
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			BufferedImage bi = ImageIO.read(bis);
			Integer srcWidth = bi.getWidth();
			Integer srcHeight = bi.getHeight();
			double wscale = srcWidth/w;
			double hscale = srcHeight/h;
			if(wscale>1 && hscale>1){
//						Thumbnails.of(bi).scale(1/wscale).outputFormat(filenameTail.substring(1)).toOutputStream(os);
				if(wscale>hscale){
					w = (int)(srcWidth/hscale);
				}else{
					h = (int)(srcHeight/wscale);
				}
		    	Image image = bi.getScaledInstance(w, h, Image.SCALE_SMOOTH);
		    	BufferedImage tag = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			    Graphics g = tag.getGraphics();
			    g.drawImage(image, 0, 0, null);
			    g.dispose();
			    ImageIO.write(tag, suffix.substring(1), os);
		    }else{
		    	os.write(bytes);
		    }
		}else{

		    os.write(bytes);
		}
	}
}
