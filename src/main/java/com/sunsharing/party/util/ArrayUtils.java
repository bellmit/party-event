package com.sunsharing.party.util;
/**
 * @author Tom
 */
public class ArrayUtils {
	public static Integer[] convertStrArrayToInt(String[]strArray){
		if(strArray!=null&&strArray.length>0){
			Integer array[]=new Integer[strArray.length];
			for(int i=0;i<strArray.length;i++){  
			    array[i]=Integer.parseInt(strArray[i]);
			}
			return array;
		}else{
			return null;
		}
	}
	static double M_PI = Math.PI;
	/**
	 * 经纬度转墨卡托
	 * @param lon 经度(lon)
	 * @param lat 纬度(lat)
	 * @return
	 */
	public static double[] lonLat2Mercator(double lon,double lat)
	 {
	  double[] xy = new double[2];
	     double x = lon *20037508.342789/180;
	     double y = Math.log(Math.tan((90+lat)*M_PI/360))/(M_PI/180);
	     y = y *20037508.34789/180;
	     xy[0] = x;
	     xy[1] = y;
	     return xy;
	 }
	 /**
	  * 墨卡托转经纬度
	  * @param mercatorX
	  * @param mercatorY
	  * @return
	  */
	 public static double[] Mercator2lonLat(double mercatorX,double mercatorY)
	 {
	  double[] xy = new double[2];
	     double x = mercatorX/20037508.34*180;
	     double y = mercatorY/20037508.34*180;
	     y= 180/M_PI*(2*Math.atan(Math.exp(y*M_PI/180))-M_PI/2);
	     xy[0] = x;
	     xy[1] = y;
	     return xy;
	 }
}
