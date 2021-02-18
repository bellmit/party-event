package com.sunsharing.party.common.ws.proxy.impl;

import com.sunsharing.collagen.data_item.Item;
import com.sunsharing.collagen.data_item.SimpleItem;
import com.sunsharing.collagen.exception.RequestException;
import com.sunsharing.collagen.request.RequestResult;
import com.sunsharing.collagen.request.RequestSetting;
import com.sunsharing.collagen.request.WebServiceProxy;
import com.sunsharing.party.ConfigParam;
import com.sunsharing.party.common.ws.proxy.ProxyService;

import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxySDK implements ProxyService {

    Logger logger = Logger.getLogger(ProxySDK.class);

    @Override
    public String request(Object[] requestParam, Class[] responseParam) throws Exception {
        //初始化入参
        // String sdkIp = (String) AirRpcClient.parMap.get("proxySdkIp");
        // int sdkPort = Integer.valueOf(AirRpcClient.parMap.get("proxySdkPort").toString());
        // String requestId = (String) AirRpcClient.parMap.get("proxySdkId");
        // String localDomain = (String) AirRpcClient.parMap.get("proxySdkDomain");
        // String wsProxyFlowId = (String) AirRpcClient.parMap.get("proxySdkTranId");
        // String proxySdkAuthorizeid = (String) AirRpcClient.parMap.get("proxySdkAuthorizeid");
        // String proxySdkMethodId = (String) AirRpcClient.parMap.get("proxySdkMethodId");
        // wsProxy.appendStringParameter("userName", repValue(requestParam[0]));
        // wsProxy.appendStringParameter("ip", repValue(requestParam[1]));
        // wsProxy.appendStringParameter("transNum", repValue(requestParam[2]));
        // wsProxy.appendStringParameter("serviceId", repValue(requestParam[3]));
        // wsProxy.appendStringParameter("methodName", repValue(requestParam[4]));
        // wsProxy.appendStringParameter("paramJson", repValue(requestParam[5]));
        // wsProxy.appendStringParameter("digestCode", repValue(requestParam[6]));
        //初始化连接参数
        RequestSetting.getInstance()
            .setIpAndPort(ConfigParam.CollaGEN_AP_IP, ConfigParam.CollaGEN_AP_PORT)
            .setSocketTimeOutInSecond(30)
            .setRequestId(ConfigParam.CollaGEN_Request_ID)
            .setLocalDomain(ConfigParam.CollaGEN_Domain)
            .setSyncWsProxyFlowId(ConfigParam.CollaGEN_WS_Proxy);

        //创建服务代理对象。I_SAPP_WS为实际调用的服务ID。
        WebServiceProxy wsProxy = new WebServiceProxy(ConfigParam.CollaGEN_Service_ID1);
        logger.info("serviceId:" + ConfigParam.CollaGEN_Service_ID1);
        //业务授权ID
        wsProxy.setAuthorizeId(ConfigParam.CollaGEN_Authorized_ID);

        String requestPattern = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + "<Request>\n"
            + "<SenderId>XM.GOV.SQ.NZZB.TEST</SenderId>\n" + "<ServiceId>{7}</ServiceId>\n" + "<Inputs>\n"
            + "  <Input name=\"userName\" type=\"string\">{0}</Input>\n"
            + "  <Input name=\"ip\" type=\"string\">{1}</Input>\n"
            + "  <Input name=\"transNum\" type=\"string\">{2}</Input>\n"
            + "  <Input name=\"serviceId\" type=\"string\">{3}</Input>\n"
            + "  <Input name=\"methodName\" type=\"string\">{4}</Input>\n"
            + "  <Input name=\"paramJson\" type=\"string\"><![CDATA[{5}]]></Input>\n"
            + "  <Input name=\"digestCode\" type=\"string\">{6}</Input>\n" + "</Inputs>\n" + "</Request>";

        String content = MessageFormat.format(requestPattern, repValue(requestParam[0]),
            repValue(requestParam[1])
            , repValue(requestParam[2]), repValue(requestParam[3]), repValue(requestParam[4]), repValue(requestParam[5])
            , repValue(requestParam[6]), ConfigParam.CollaGEN_Service_ID1);

        logger.info(content);
        wsProxy.appendStringParameter("context", content);
        String str = sendWS(wsProxy);
        return str;
    }

    private String repValue(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public String sendWS(WebServiceProxy wsProxy) throws RequestException {
        String str = "";
        logger.info("发送请求到协同平台===================");
        RequestResult ret = wsProxy.request();//发送请求。
        List<Item> retList = ret.getRetItems();//获取返回结果。
        logger.info("收到结果,协同平台反馈结果数===================" + retList.size());
        if (retList.size() > 0) {
            Item s = retList.get(0);//获取返回结果第一个值，有多个值可以使用for循环。
            if (s instanceof SimpleItem) {//判断返回结果是否是简单类型
                //把结果值转换成字符串。
                str = ((SimpleItem) s).getValueIfString("");
                List<String> list = match(str, "Output");
                str = list.size() > 0 ? list.get(0) : str;
                if (hasCDATA(str)) {
                    str = replaceCDATA2(str);
                }
            }
        }
        logger.info("接收协同平台报文:" + str);
        return str;
    }

    public static List<String> match(String source, String element) {
        List<String> result = new ArrayList<String>();
        String reg = "<" + element + ".*?>([\\s\\S]*?)</" + element + ">";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            String r = m.group(1);
            result.add(r);
        }
        return result;
    }

    public static boolean hasCDATA(String xml) {
        String regx = "<!\\[CDATA\\[[\\s\\S]*?]]>";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(xml);
        return matcher.find();
    }

    public String replaceCDATA2(String str) {
        logger.info("需要替换<![CDATA[~]]>：" + str);
        boolean falg = hasCDATA(str);
        if (falg && str.startsWith("<![CDATA[")) {
            str = str.substring(str.indexOf("<![CDATA[") + 9, str.length());
            if (str.endsWith("]]>")) {
                str = str.substring(0, str.lastIndexOf("]]>"));
            } else if (str.endsWith("]]]]>>")) {
                str = str.substring(0, str.lastIndexOf("]]]]>>"));
            }
        }
        if (str.startsWith("<![CDATA[") && hasCDATA(str)) {
            str = replaceCDATA2(str);
        }
        return str;
    }

}
