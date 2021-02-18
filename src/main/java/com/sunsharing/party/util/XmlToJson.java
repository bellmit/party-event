package com.sunsharing.party.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSON;
import net.sf.json.util.JSONUtils;
import net.sf.json.xml.XMLSerializer;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;

import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.StringUtils;

public class XmlToJson {

	/** 
	 * 将xml字符串转换为JSON对象 
	 * @param
	 * @return JSON对象 
	 */  
	public JSON getJSONFromXml(String xmlString) {  
	    XMLSerializer xmlSerializer = new XMLSerializer();  
	    JSON json = xmlSerializer.read(xmlString);  
	    return json;  
	}  
	/** 
	 * 将xmlDocument转换为JSON对象 
	 * @param xmlDocument XML Document 
	 * @return JSON对象 
	 */  
	public JSON getJSONFromXml(Document xmlDocument) {  
	    String xmlString = xmlDocument.toString();  
	    return getJSONFromXml(xmlString);  
	}  
	/** 
	 * 将xml字符串转换为JSON字符串 
	 * @param xmlString 
	 * @return JSON字符串 
	 */  
	public String getJSONStringFromXml(String xmlString ) {  
	    return getJSONFromXml(xmlString).toString();  
	}  
	/** 
	 * 将xmlDocument转换为JSON字符串 
	 * @param xmlDocument XML Document 
	 * @return JSON字符串 
	 */  
	public String getXMLtoJSONString(Document xmlDocument) {  
	    return getJSONStringFromXml(xmlDocument.toString());  
	}  
	/** 
	 * 读取XML文件准换为JSON字符串 
	 * @param xmlFile  XML文件 
	 * @return JSON字符串 
	 */  
	public String getXMLFiletoJSONString(String xmlFile) {  
	    InputStream is = JSONUtils.class.getResourceAsStream(xmlFile);  
	    String xml;  
	    JSON json = null;  
	    try {  
	        xml = IOUtils.toString(is);  
	        XMLSerializer xmlSerializer = new XMLSerializer();  
	        json = xmlSerializer.read(xml);  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	    return json.toString();  
	}  
	/** 
	 * 将Map准换为JSON字符串 
	 * @param map 
	 * @return JSON字符串 
	 */  
	public static  String getJsonStringFromMap(Map<?, ?> map) {  
	    JSONObject object = (JSONObject)JSONObject.toJSON(map);  
	    return object.toString();  
	}  
	public static boolean hasCDATA(String xml)
	{
		String regx = "<!\\[CDATA\\[[\\s\\S]*?]]>";
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(xml);
//		StringBuffer sBuffer = new StringBuffer();
//		 while (matcher.find())
//		 {
//			 matcher.appendTail(sBuffer);
//		 }
//		 Pattern p = Pattern.compile(".*<!\\[CDATA\\[(.*)\\]\\]>.*");
//		   Matcher m = p.matcher(sBuffer.toString());
//		    
//		   if(m.matches()) {
//		    System.out.println(m.group(1));
//		   }
		return matcher.find();
	}
	public static JSONObject ConvertXMLtoJSON(String xml)  {  
//		StringBuffer sb = new StringBuffer(xml);
//        InputStream is = ConvertXMLtoJSON.class.getResourceAsStream("sample.xml");  
//        String xml1;  
        try {  
//            xml = IOUtils.toString(is);
            XMLSerializer xmlSerializer = new XMLSerializer();  
            JSON json = xmlSerializer.read(xml);
            JSONObject jsonObject = JSONObject.parseObject(json.toString(1));
            String extend = jsonObject.getString("extend");
            if(!StringUtils.isBlank(extend)&&!"[]".equals(extend))
            {
	            json = xmlSerializer.read(extend);
	            jsonObject.put("extend", json);
            }
            String extend2 = jsonObject.getString("extend2");
            if(!StringUtils.isBlank(extend2)&&!"[]".equals(extend2))
            {
	            json = xmlSerializer.read(extend2);
	            jsonObject.put("extend2", json);
            }
            return jsonObject;
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;
    }  
      
    public static void main(String[] args) {  
        ConvertXMLtoJSON("");  
    } 
}
