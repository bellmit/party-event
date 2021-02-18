/**
 * @(#)$CurrentFile
 * 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 *
 *<br> Copyright:  Copyright (c) 2013
 *<br> Company:厦门畅享信息技术有限公司
 *<br> @author criss
 *<br> 13-9-11 下午2:30
 *<br> @version 1.0
 *————————————————————————————————
 *修改记录
 *    修改者：
 *    修改时间：
 *    修改原因：
 *————————————————————————————————
 */
package com.sunsharing.party.common.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * <pre></pre>
 * <br>----------------------------------------------------------------------
 * <br> <b>功能描述:</b>
 * <br>
 * <br> 注意事项:
 * <br>
 * <br>
 * <br>----------------------------------------------------------------------
 * <br>
 */
public class ResponseHelper {
    static Logger logger = Logger.getLogger(ResponseHelper.class);

    /** 输出特定json对象的响应流 */
    public static void printOut(HttpServletResponse response, String content) {
        printOutContent(response, covert2Json(true, "", content));
    }
    public static void printOut(HttpServletResponse response, Map map) {
        printOutContent(response,covert2Json(true, "", map));
    }
    public static void printOut(HttpServletResponse response, List list) {
        printOutContent(response,covert2Json(true, "", list));
    }
    public static void printOut(HttpServletResponse response, JSONObject json) {
        printOutContent(response,covert2Json(true, "", json));
    }
    public static void printOut(HttpServletResponse response, JSONArray json) {
        printOutContent(response,covert2Json(true, "", json));
    }
    public static void printOut(HttpServletResponse response, JsonResponse jsonResponse) {
        printOutContent(response, covert2Json(jsonResponse));
    }

    /** 输出业务错误的响应流 */
    public static void printOutBusinessError(HttpServletResponse response, String errorMsg) {
        printOutContent(response, covert2Json(false, errorMsg, null));
    }
    public static void printOutBusinessError(HttpServletResponse response, String errorMsg,String errorCode) {
        printOutContent(response, covert2Json(false, errorMsg, null,errorCode));
    }

    public static void printOutJSONP(HttpServletResponse response,String callback, String content) {
        printOutContent(response, callback + "(" + content + ");");
    }



    /**
     * 输出响应流
     * @param response
     * @param content：要输出的内容
     * @return
     */
    public static void printOutContent(HttpServletResponse response, String content) {
    	response.reset();
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            // response.setContentLength(responseContent.length());
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            logger.error("消息输出出错", e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
    /**
     * 组装返回的json对象
     * @param status
     * @param msg
     * @param o
     * @return
     */
    private static String covert2Json(boolean status,String msg,Object o){
        return covert2Json(status,msg,o,"");
    }
    private static String covert2Json(boolean status,String msg,Object o,String code){
        JsonResponse jr = new JsonResponse();
        jr.setStatus(status);
        if(!"".equals(code)){
            jr.setCode(code);
        }
        if(!"".equals(msg)){
            jr.setMsg(msg);
        }
        jr.setData(o);
        return covert2Json(jr);
    }
    /**
     * 组装返回的json对象
     * @param jsonResponse
     * @return
     */
    private static String covert2Json(JsonResponse jsonResponse){
        return JSON.toJSONString(jsonResponse);
    }
}

