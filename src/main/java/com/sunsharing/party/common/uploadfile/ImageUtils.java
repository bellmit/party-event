package com.sunsharing.party.common.uploadfile;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: kakutcho
 * Date: 13-8-8
 * Time: 上午11:12
 * To change this template use File | Settings | File Templates.
 */
public class ImageUtils {
    private static Logger logger = Logger.getLogger(ImageUtils.class);
    /*常用图片格式*/
    public static String BMP = "bmp";
    public static String JPG = "jpg";
    public static String JPEG = "jpeg";
    public static String PNG = "png";
    public static String GIF = "gif";

    /**
     * 取得图片的360x200的文件名
     * @param localImageFile
     * @return
     */
    public static String getImageName360x200(String localImageFile){
        int i = localImageFile.lastIndexOf(".");
        String localImgTemp1 = localImageFile.substring(0,i);
        String localImgTemp2 = localImageFile.substring(i);

        String localImg360x200 = localImgTemp1+"360x200"+ localImgTemp2;
        return localImg360x200;
    }
    /**
     * 取得图片的尺寸自定义的文件名（200x200，200*300）
     * @param localImageFile
     * @param imageSize 例如：200x200 300*300
     * @return
     */
    public static String getImageName(String imageSize,String localImageFile){
        int i = localImageFile.lastIndexOf(".");
        String localImgTemp1 = localImageFile.substring(0,i);
        String localImgTemp2 = localImageFile.substring(i);
        String localImg200x200 = localImgTemp1+imageSize+ localImgTemp2;
        return localImg200x200;
    }
    /**
     * 将文件流保存为图片
     * @param image 图片字节数组
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    public static BufferedImage scaleImage(byte[] image,int width,int height) throws IOException{
        ByteArrayOutputStream bos = null;
        ImageOutputStream ios = null;
        try {
            //处理图片
            ByteArrayInputStream in = new ByteArrayInputStream(image);
            BufferedImage bufferedImage = ImageIO.read(in);  //将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
            int w = bufferedImage.getWidth();
            if(w>width){
                double double1 = w/width;
                w = width;
                bufferedImage = Thumbnails.of(bufferedImage)
                        .scale(1/double1)
                        .asBufferedImage();
            }
            return Thumbnails.of(bufferedImage)
                    .sourceRegion(Positions.CENTER,w,height)
                    .size(w,height)
                    .asBufferedImage();
        } catch (IOException e) {
            logger.error("剪裁图片失败！",e);
            throw e;
        } finally {
            if(bos!=null){
                bos.close();
            }
            if(ios!=null){
                ios.close();
            }
        }
    }
    /**
     * 斜水印,重复水印,文字
     * @param pressText  文字
     * @param targetImg  目标图片
     * @param colorStr 字体颜色字符串，格式如：#29944f
     * @param startY  左上起y的距离
     * @param alpha 透明度(0.1-0.9)
     * @param carelessness true为字体实心,false为字体空心
     * @return
     */
    public static BufferedImage pressText(String pressText, String targetImg,
                                          Font font, String colorStr,float startY,double rotate,
                                          float alpha,boolean carelessness) throws Exception{

        File file = new File(targetImg);
        Image src = ImageIO.read(file);
        return pressText(pressText,src,font,colorStr,startY,rotate,alpha,carelessness);
    }

    /**
     * 斜水印,重复水印,文字
     * @param pressText  文字
     * @param imgBytes  图片字节流
     * @param colorStr 字体颜色字符串，格式如：#29944f
     * @param startY  左上起y的距离
     * @param alpha 透明度(0.1-0.9)
     * @param carelessness true为字体实心,false为字体空心
     * @return
     */
    public static BufferedImage pressText(String pressText, byte[] imgBytes,
                                          Font font, String colorStr,float startY,double rotate,
                                          float alpha,boolean carelessness) throws Exception{
        ByteArrayInputStream in = new ByteArrayInputStream(imgBytes);    //将b作为输入流；
        Image image = ImageIO.read(in);     //将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
        return pressText(pressText,image,font,colorStr,startY,rotate,alpha,carelessness);
    }

    /**
     * 斜水印,重复水印,文字
     * @param pressText  文字
     * @param bufferedImage  图片BufferedImage
     * @param colorStr 字体颜色字符串，格式如：#29944f
     * @param startY  左上起y的距离
     * @param alpha 透明度(0.1-0.9)
     * @param carelessness true为字体实心,false为字体空心
     * @return
     */
    public static BufferedImage pressText(String pressText, Image bufferedImage,
                                          Font font, String colorStr,float startY,double rotate,
                                          float alpha,boolean carelessness) throws Exception{
        //图片宽度
        int width = bufferedImage.getWidth(null);
        //图片高度
        int height = bufferedImage.getHeight(null);
        BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d  = image.createGraphics();
        //绘原图
        g2d.drawImage(bufferedImage, 0, 0, width, height, null);
        //比例
        g2d.scale(1, 1);

        g2d.addRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //颜色
        Color color = Color.decode(colorStr);

        //字体

        GlyphVector fontGV = font.createGlyphVector(g2d.getFontRenderContext(),
                pressText);
        Rectangle size = fontGV
                .getPixelBounds(g2d.getFontRenderContext(), 0, 0);
        Shape textShape = fontGV.getOutline();
        double textWidth = size.getWidth();
        double textHeight = size.getHeight();
        double jd = rotate*Math.PI/180;
        AffineTransform rotate45 = AffineTransform
                .getRotateInstance(jd);
        Shape rotatedText = rotate45.createTransformedShape(textShape);
        // use a gradient that repeats 4 times
        g2d.setPaint(new GradientPaint(0, 0, color,
                image.getWidth(), image.getHeight(),color));

        //透明度
        g2d.setStroke(new BasicStroke(alpha));

        // step in y direction is calc'ed using pythagoras + 5 pixel padding
        // double x = startY/Math.tan(rotate);// Math.sqrt(textWidth * textWidth / 2) + 5;
        double y = startY ;
        double stepY = textWidth * Math.sin(jd)+ 5;
        double x = textWidth * Math.cos(jd)+ 5;
        g2d.translate(0, y);
        for (; y < image.getHeight();y += stepY) {
            g2d.draw(rotatedText);
            if(carelessness)//字体实心
            {
                g2d.fill(rotatedText);
            }
            g2d.translate(x, stepY);
        }
        g2d.dispose();

        return image;
    }

    public static void main(String[] args) throws Exception{
//        Font font = new Font("宋体", Font.PLAIN, 16);
//        BufferedImage bufferedImage = ImageUtils.pressText("畅享信息","c:/zp.jpg", font,"0x2996dd",65,25,0.6f,false);
//        Thumbnails.of(bufferedImage).size(bufferedImage.getWidth(),bufferedImage.getHeight()).toFile("c:/zpsssss.jpg");

    }
}
