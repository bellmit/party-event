package com.sunsharing.party.util;

import com.sunsharing.common.utils.StringUtils;

public class TagElUtil {

	public static String tranTimeStr(String time){
		return toDisplayDateStr(time, "", false);
	}
	
	/**
	 * 字符串转换时间格式
	 * @param dateStr 日期字符串
	 * @param formatType 日期格式
	 * @param isFill 是否填充补足全日期
	 * @return 日期字符串
	 */
	public static String toDisplayDateStr(String dateStr,String formatType,boolean isFill){
		if (StringUtils.isBlank(dateStr) || dateStr.length() < 6 || dateStr.length() > 14)
        {
            return dateStr;
        }
		String[] charArr = null;
        if(formatType.equals("HY")){
                charArr = new String[]{"-", "-", " ", ":", ":"};
        }else if(formatType.equals("DOT")){
                charArr = new String[]{".", ".", " ", ":", ":"};
        }else if(formatType.equals("CN")){
                charArr =new String[]{"年", "月", "日 ", ":", ":"};
        }else
            charArr =new String[]{"-", "-", " ", ":", ":"};
        try
        {
            dateStr = dateStr.replace(" ","").replace("-","").replace(":","");
            switch (dateStr.length())
            {
                case 6:
                    dateStr = dateStr.substring(0,4) + charArr[0] + dateStr.substring(4,6);
                    break;
                case 8:
                    dateStr = dateStr.substring(0,4) + charArr[0] + dateStr.substring(4,6) + charArr[1] + dateStr.substring(6,8);
                    break;
                case 10:
                    dateStr = dateStr.substring(0,4) + charArr[0] + dateStr.substring(4,6) + charArr[1] + dateStr.substring(6,8) 
                    + charArr[2] + dateStr.substring(8,10);
                    break;
                case 12:
                    dateStr = dateStr.substring(0,4) + charArr[0] + dateStr.substring(4,6) + charArr[1] +
                        dateStr.substring(6,8) + charArr[2] + dateStr.substring(8,10) + charArr[3] + dateStr.substring(10,12);
                    break;
                case 14:
                    dateStr = dateStr.substring(0,4) + charArr[0] + dateStr.substring(4,6) + charArr[1] +
                        dateStr.substring(6,8) + charArr[2] + dateStr.substring(8,10) + charArr[3] + dateStr.substring(10,12) +
                        charArr[4] + dateStr.substring(12,14) ;
                    break;
                default:
                    return dateStr;
            }
            if(isFill){
            	switch (dateStr.length())
                {
                    case 6:
                    	dateStr += charArr[1]+"01"+charArr[2]+"23"+charArr[3]+"59"+charArr[4]+"59";
                    	break;
                    case 8:
                    	dateStr += charArr[2]+"23"+charArr[3]+"59"+charArr[4]+"59";
                    	break;
                    case 10:
                    	dateStr += charArr[3]+"59"+charArr[4]+"59";
                    	break;
                    case 12:
                    	dateStr += charArr[4]+"59";
                    	break;
                }
            }
            return dateStr;
        }
        catch (Exception ex)
        {
            return dateStr;
        }
	}
}
