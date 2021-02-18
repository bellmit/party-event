package com.sunsharing.party.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunsharing.component.utils.base.StringUtils;

/**
 * Created by criss on 13-12-23.
 */
public class HttpSendShort {

	static Logger logger = Logger.getLogger(HttpSendShort.class);

	public static JSONObject sendByHttpClientCharset(String body, String wsdl, String charset) throws Exception {
		final StringBuffer sb = new StringBuffer();
		sendStrOfPost(wsdl, body, charset, sb, "");
		return XmlToJson.ConvertXMLtoJSON(sb.toString());
	}

	public static String sendByHttpClient(String body, String wsdl, String action) throws Exception {
		return sendByHttpClient(body, wsdl, action, "");
	}

	public static String sendByHttpClient(String body, String wsdl, String action, String charset) throws Exception {
		logger.info("发送报文：" + body);
		if (wsdl.endsWith("wsdl")) {
			wsdl = wsdl.substring(0, wsdl.length() - 5);
		}
		if (wsdl.endsWith("WSDL")) {
			wsdl = wsdl.substring(0, wsdl.length() - 5);
		}
		final StringBuffer sb = new StringBuffer();
		sendStrOfPost(wsdl, body, charset, sb, action);
		logger.info("返回报文：" + sb.toString());
		try {
			return writeWrite(sb);
		} catch (final Exception e) {
			logger.error("解析XML错误", e);
			return sb.toString();
		}
	}

	/**
	 * 以URL方式发送数据
	 * 
	 * @param urlStr
	 *            发送地址
	 * @param contentStr
	 *            发送内容
	 * @param charset
	 *            发送字符集
	 * @param sResult
	 *            返回数据Buffer
	 * @return boolean 发送是否成功
	 */
	public static boolean sendStrOfPost(String urlStr, String contentStr, String charset, StringBuffer sResult,
			String action) {
		boolean bResult = false;
		final String charsetName = charset;
		URL url = null;
		HttpURLConnection httpConnection = null;
		InputStream httpIS = null;
		BufferedReader http_reader = null;
		try {
			url = new URL(urlStr);
			httpConnection = (HttpURLConnection) url.openConnection();

			// 设置连接主机超时(单位:毫秒)
			httpConnection.setConnectTimeout(10000);
			// 设置从主机读取数据超时(单位:毫秒)
			httpConnection.setReadTimeout(20000);

			httpConnection.setRequestMethod("POST"); // POST方式提交数据
			httpConnection.setDoOutput(true);
			httpConnection.setRequestProperty("Content-Length", String.valueOf(contentStr.getBytes().length));
			httpConnection.setRequestProperty("SOAPAction", action);
			if (!StringUtils.isBlank(action)) {
				httpConnection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
			}
			// , soapAction
			PrintWriter out = null;
			out = new PrintWriter(new OutputStreamWriter(httpConnection.getOutputStream(), charsetName));// 此处改动
			// 发送请求
			out.print(contentStr);
			out.flush();
			out.close();
			final int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				// 发送正常
				bResult = true;

				// 读取数据
				httpIS = httpConnection.getInputStream();
				http_reader = new BufferedReader(new InputStreamReader(httpIS, charsetName));
				String line = null;
				while ((line = http_reader.readLine()) != null) {
					if (sResult.length() > 0) {
						sResult.append("\n");
					}
					sResult.append(line);
				}
				logger.info("[URL][response][success]" + sResult);
			} else {
				logger.info("[URL][response][failure][code : " + responseCode + " ]");
			}
		} catch (final Exception e) {
			logger.error("[HttpUtil]sendStrOfPost error", e);
			throw new RuntimeException("调用服务出错");
		} finally {
			try {
				if (http_reader != null) {
					try {
						http_reader.close();
					} catch (final Exception e) {

					}
				}
				if (httpIS != null) {
					try {
						httpIS.close();
					} catch (final Exception e) {

					}
				}
				if (httpConnection != null) {
					httpConnection.disconnect();
				}
			} catch (final Exception e) {
				logger.error("[HttpUtil]finally error", e);
			}
		}

		return bResult;
	}

	public static String writeWrite(StringBuffer buffer) throws Exception {
		final Document doc = DocumentHelper.parseText(buffer.toString());

		final HashMap xmlMap = new HashMap();
		xmlMap.put("env", "http://www.w3.org/2003/05/soap-envelope");
		final XPath x = doc.createXPath("//env:Body");
		x.setNamespaceURIs(xmlMap);

		Element result = (Element) x.selectSingleNode(doc);
		if (result == null) {
			final HashMap xmlMap2 = new HashMap();
			xmlMap2.put("env", "http://schemas.xmlsoap.org/soap/envelope/");
			final XPath x2 = doc.createXPath("//env:Body");
			x2.setNamespaceURIs(xmlMap2);
			result = (Element) x2.selectSingleNode(doc);
		}
		if (result == null) {
			final JSONObject object = new JSONObject();
			analyzeXml(doc.getRootElement(), object, null);
			return object.toJSONString();
		}
		final List list = result.elements();
		if (list.size() > 0) {
			final Element lll = (Element) list.get(0);
			Element list2 = null;
			if ((lll).elements().size() > 0) {
				list2 = (Element) ((lll).elements().get(0));
				if (list2.elements().size() == 0) {
					return list2.getText();
				} else {
					final Document returnDoc = DocumentHelper.createDocument();
					returnDoc.add((Element) list2.clone());
					final OutputFormat format = new OutputFormat("    ", true);
					// 设置编码
					format.setEncoding("UTF-8");
					// xml输出器
					final StringWriter out = new StringWriter();
					final XMLWriter xmlWriter = new XMLWriter(out, format);
					// 打印doc
					xmlWriter.write(returnDoc);
					xmlWriter.flush();
					// 关闭输出器的流，即是printWriter
					final String s = out.toString();
					out.close();
					return s;
				}
			} else {
				return lll.getText();
			}

			// Document returnDoc = DocumentHelper.createDocument();
			// returnDoc.add((org.dom4j.Element)list2.clone());
			// OutputFormat format = new OutputFormat(" ", true);
			// //设置编码
			// format.setEncoding("UTF-8");
			// //xml输出器
			// StringWriter out = new StringWriter();
			// XMLWriter xmlWriter = new XMLWriter(out, format);
			// //打印doc
			// xmlWriter.write(returnDoc);
			// xmlWriter.flush();
			// //关闭输出器的流，即是printWriter
			// String s = out.toString();
			// out.close();
			// return list2.toString();
		}
		return buffer.toString();
	}

	/**
	 * 转换XML为JSON字符串 （注：相同属性会被覆盖）
	 * 
	 * @param ele
	 * @param object
	 * @param array
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void analyzeXml(Element ele, JSONObject object, JSONArray array) {
		for (final Iterator i = ele.elementIterator(); i.hasNext();) {
			final Element node = (Element) i.next();
			// System.out.println("节点名:"+node.getName());
			if (node.attributes() != null && node.attributes().size() > 0) {
				final JSONArray array2 = new JSONArray();
				for (final Iterator j = node.attributeIterator(); j.hasNext();) {
					final Attribute item = (Attribute) j.next();
					final JSONObject object2 = new JSONObject();
					object2.put(item.getName(), item.getValue());
					array2.add(object2);
				}
				object.put(node.getName(), array2);
			}
			if (node.getText().length() > 0) {
				object.put(node.getName(), node.getText());
			}
			if (node.elementIterator().hasNext()) {
				array = new JSONArray();
				final JSONObject object2 = new JSONObject();
				analyzeXml(node, object2, array);
				object.put(node.getName(), array);
				array = null;
			}
		}
		if (array != null) {
			array.add(object);
		}
	}

	public static String getHTML(String httpUrl, String Charset) {
		String html = "";

		try {
			final URL url = new URL(httpUrl.toString());
			final StringBuffer document = new StringBuffer();
			try {
				final URLConnection urlCon = url.openConnection();
				logger.info("获取到URLConnection：" + urlCon);
				final BufferedReader reader = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
				logger.info("获取到BufferedReader：" + reader);
				String Result = "";
				while ((Result = reader.readLine()) != null) {
					document.append(Result);
				}

				html = document.toString();
				logger.info("获取到html：" + html);
			} catch (final IOException e) {
				System.out.println(e);
				html = "服务未响应";

			}
		} catch (final MalformedURLException e) {
			html = "不支持的协议";

		}

		return html;
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param param
	 *            封装一个 JSON 对象
	 * @return JSONObject
	 * @throws Exception
	 */
	public static JSONObject sendPost(String url, JSONObject param) {
		try {
			final HttpPost request = new HttpPost(url);
			// 绑定到请求 Entry
			final StringEntity se = new StringEntity(param.toString(), Charsets.UTF_8);
			request.setEntity(se);
			logger.info("开始准备发送sendPost请求到微信....");
			final HttpResponse httpResponse = new DefaultHttpClient().execute(request);
			// 得到应答的字符串，这也是一个 JSON 格式保存的数据
			final String retSrc = EntityUtils.toString(httpResponse.getEntity(), Charsets.UTF_8);
			logger.info("结束发送sendPost请求到微信->返回：" + retSrc);
			// 生成 JSON 对象
			final JSONObject jobj = JSON.parseObject(retSrc);
			return jobj;
		} catch (final Exception e) {
			logger.info(e);
			e.printStackTrace();
		}
		return null;

	}

}
