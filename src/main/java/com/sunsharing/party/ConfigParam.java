/**
 * @(#)ConfigParam 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 *
 *                 <br>
 *                 Copyright: Copyright (c) 2014 <br>
 *                 Company:厦门畅享信息技术有限公司 <br>
 * @author ulyn <br>
 *         14-1-9 下午2:49 <br>
 * @version 1.0 ———————————————————————————————— 修改记录 修改者： 修改时间： 修改原因： ————————————————————————————————
 */
package com.sunsharing.party;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.sunsharing.component.resvalidate.config.annotation.Configuration;
import com.sunsharing.component.resvalidate.config.annotation.ParamField;
import com.sunsharing.component.resvalidate.config.annotation.validate.NumValidate;

/**
 * <pre></pre>
 *
 * <br>
 * ---------------------------------------------------------------------- <br>
 * <b>功能描述:</b> <br>
 * <br>
 * 注意事项: <br>
 * <br>
 * <br>
 * ---------------------------------------------------------------------- <br>
 */
@Configuration(value = "config.properties")
public class ConfigParam {

	/** =================系统配置start ========================= **/

	@ParamField(name = "memcached.host", required = false)
	public static String memcachedHost = "127.0.0.1:11211";
	// 配置的项目名称，用于区分项目
	@ParamField(name = "entry_name")
	public static String entryName;
	// 本机ip
	@ParamField(name = "local_ip")
	public static String localIp;
	// 配置系统超时时间秒(Session)
	@ParamField(name = "SysTimeOut", required = false)
	@NumValidate
	public static String sysTimeOut = "1000";
	// #air服务端的请求地址，不带wsdl
	@ParamField(name = "air_target_url")
	public static String airTargetUrl;
	// #air{直接连接air-server},sdk{协同平台}
	@ParamField(name = "proxy_type")
	public static String proxyType;
	@ParamField(name = "option_timeout")
	public static String optionTimeout;
	@ParamField(name = "test_network_url")
	public static String testNetworkUrl;
	@ParamField(name = "normal_url")
	public static String normalUrl;// 普通版地址
	@ParamField(name = "normal_net_url")
	public static String normalNetUrl;// 外网IP
	@ParamField(name = "normal_inner_url")
	public static String normalInnerUrl;// 内网IP
	/** =================系统配置end ========================= **/

	/** =================微信开发相关配置start ========================= **/
	// 微信服务器URL
	@ParamField(name = "weixinurl")
	public static String weixinurl;
	// #企业号开发配置
	@ParamField(name = "CorpID")
	public static String CorpID = "";
	@ParamField(name = "Secret")
	public static String Secret = "";
	@ParamField(name = "agentid")
	public static String agentid = "";
	@ParamField(name = "department_id")
	public static String department_id = "";
	@ParamField(name = "sEncodingAESKey")
	public static String sEncodingAESKey = "";
	@ParamField(name = "sToken")
	public static String sToken = "";
	// 加密的key
	@ParamField(name = "Key")
	public static String Key;
	// 公众号配置
	@ParamField(name = "weixin_appid")
	public static String weixinAppId = "";
	@ParamField(name = "weixin_secretkey")
	public static String weixinSecretKey = "";
	/** =================微信开发相关配置end ========================= **/

	/** =================HDFS相关配置start ========================= **/
	// 配置的图片类型文件
	@ParamField(name = "img_type")
	public static String imgType;
	// hdfs的使用
	@ParamField(name = "hadoop_uri")
	public static String hadoopUri = "";

	@ParamField(name = "hadoop_account")
	public static String hadoopAccount = "";
	// 文件存储类型
	@ParamField(name = "file_server_type")
	public static String fileServerType;
	// dfs服务器实际服务器ip地址
	@ParamField(name = "file_server_dfs_real_ip")
	public static String fileServerDfsRealIp;
	// dfs服务器代理IP地址
	@ParamField(name = "file_server_dfs_proxy_ip")
	public static String fileServerDfsProxyIp;
	// dfs服务器端口
	@ParamField(name = "file_server_dfs_port")
	public static int fileServerDfsPort;
	// jfs保存文件的本机路径
	@ParamField(name = "file_server_jfs_path")
	public static String fileServerJsfPath;
	/** =================HDFS相关配置end ========================= **/

	@ParamField(name = "ftp_ip_port")
	public static String ftpIpPort;
	@ParamField(name = "ftp_path")
	public static String ftpPath;
	@ParamField(name = "ftp_user")
	public static String ftpUser;
	@ParamField(name = "ftp_pwd")
	public static String ftpPwd;
	@ParamField(name = "S_path")
	public static String Spath;

	/** =================协同平台相关配置start ========================= **/
	// 请求方ip
	@ParamField(name = "collagen_ap_ip")
	public static String CollaGEN_AP_IP = "";
	// 请求方port
	@ParamField(name = "collagen_ap_port")
	public static int CollaGEN_AP_PORT = 8081;
	// 请求方id
	@ParamField(name = "collagen_request_id")
	public static String CollaGEN_Request_ID = "";
	// 域LocalDomain
	@ParamField(name = "collagen_domain")
	public static String CollaGEN_Domain = "";
	// 流程ID
	@ParamField(name = "collagen_ws_proxy")
	public static String CollaGEN_WS_Proxy = "";
	// authorizeid(授权ID)
	@ParamField(name = "collagen_authorized_id")
	public static String CollaGEN_Authorized_ID = "";
	// 请求超时时间
	@ParamField(name = "collagen_request_timeout")
	public static int CollaGEN_Request_TimeOut = 20;
	// 服务id
	@ParamField(name = "collagen_service_id1")
	public static String CollaGEN_Service_ID1 = "";
	// 服务id
	@ParamField(name = "collagen_service_id2")
	public static String CollaGEN_Service_ID2 = "";
	// 服务id
	@ParamField(name = "collagen_service_id3")
	public static String CollaGEN_Service_ID3 = "";
	// 服务id
	@ParamField(name = "collagen_service_id4")
	public static String CollaGEN_Service_ID4 = "";
	// 服务id
	@ParamField(name = "collagen_service_id5")
	public static String CollaGEN_Service_ID5 = "";
	// 服务id
	@ParamField(name = "collagen_service_id6")
	public static String CollaGEN_Service_ID6 = "";
	// 服务id
	@ParamField(name = "collagen_service_id7")
	public static String CollaGEN_Service_ID7 = "";
	// 服务id
	@ParamField(name = "collagen_service_id8")
	public static String CollaGEN_Service_ID8 = "";
	// 服务id
	@ParamField(name = "collagen_service_id9")
	public static String CollaGEN_Service_ID9 = "";
	// 服务id
	@ParamField(name = "collagen_service_id10")
	public static String CollaGEN_Service_ID10 = "";
	// 服务id
	@ParamField(name = "collagen_service_id11")
	public static String CollaGEN_Service_ID11 = "";
	// 服务id
	@ParamField(name = "collagen_service_id12")
	public static String CollaGEN_Service_ID12 = "";
	// 服务id
	@ParamField(name = "collagen_service_id13")
	public static String CollaGEN_Service_ID13 = "";
	// 服务id
	@ParamField(name = "collagen_service_id14")
	public static String CollaGEN_Service_ID14 = "";
	// 服务id
	@ParamField(name = "collagen_service_id15")
	public static String CollaGEN_Service_ID15 = "";
	// 服务id
	@ParamField(name = "collagen_service_id16")
	public static String CollaGEN_Service_ID16 = "";
	// 服务id
	@ParamField(name = "collagen_service_id17")
	public static String CollaGEN_Service_ID17 = "";
	// 服务id
	@ParamField(name = "collagen_service_id18")
	public static String CollaGEN_Service_ID18 = "";
	// 服务id
	@ParamField(name = "collagen_service_id19")
	public static String CollaGEN_Service_ID19 = "";
	// 服务id
	@ParamField(name = "collagen_service_id20")
	public static String CollaGEN_Service_ID20 = "";
	// 服务id
	@ParamField(name = "collagen_service_id21")
	public static String CollaGEN_Service_ID21 = "";
	// 服务id
	@ParamField(name = "collagen_service_id22")
	public static String CollaGEN_Service_ID22 = "";
	// 服务id
	@ParamField(name = "collagen_service_id23")
	public static String CollaGEN_Service_ID23 = "";
	/** =================协同平台相关配置end ========================= **/
	@ParamField(name = "mh_role_id")
	public static String mhRoleId = "";
	@ParamField(name = "shgl_legend_url")
	public static String shglLegendUrl; // 社会管理系统legend-server地址

	@ParamField(name = "xf_dw_id")
	public static String xf_dw_id; // 消防部门dwid

	@ParamField(name = "map_url_gis")
	public static String mapUrlGis; // gis地图地址

	@ParamField(name = "server_url_gis")
	public static String serverUrlGis; // gis服务器地址

	@ParamField(name = "sat_url_gis")
	public static String satUrlGis; // 矢量图地址

	@ParamField(name = "tile_url_gis")
	public static String tileUrlGis; // 卫星图地址

	@ParamField(name = "road_url_gis")
	public static String roadUrlGis; // 路网图地址

	@ParamField(name = "is_use_jm_local_layer")
	public static String isUseJmLocalLayer; // 是否使用精图图层,1是0不是,优先级低于isUseJtLocalLayer

	@ParamField(name = "is_use_jt_local_layer")
	public static String isUseJtLocalLayer; // 是否使用精图图层,1是0不是,优先级高于isUseJmLocalLayer

	@ParamField(name = "jm_area_layer_url")
	public static String jmAreaLayerUrl; // 是否使用精图图层,1是0不是,优先级低于isUseJtLocalLayer

	@ParamField(name = "jt_area_layer_url")
	public static String jtAreaLayerUrl; // 是否使用精图图层,1是0不是,优先级高于isUseJmLocalLayer

	@ParamField(name = "is_use_tdt")
	public static String isUseTDT; // 是否使用天地图精图模式

	@ParamField(name = "coordinate_system")
	public static String coordinateSystem; // 图层坐标系（投影坐标系[在线网络坐标系]=102113/102100,地理坐标系[GPS,绘图等]=4326/4490）

	@ParamField(name = "amap_web_url")
	public static String amapWebUrl = "";

	@ParamField(name = "amap_web_url_key")
	public static String amapWebUrlKey = "";

	@ParamField(name = "amap_web_ip")
	public static String amap_webIp = "";

	@ParamField(name = "website")
	public static String website = "";
	@ParamField(name = "app_id")
	public static String app_id = "";
	@ParamField(name = "modle_id")
	public static String modle_id = "";
	@ParamField(name = "appId")
	public static String appId = "";
	@ParamField(name = "secretKey")
	public static String secretKey = "";
	@ParamField(name = "smsSendUrl")
	public static String smsSendUrl = "";
	@ParamField(name = "tianHaiCss")
	public static String tianHaiCss = "";
	@ParamField(name = "tianHaiApi")
	public static String tianHaiApi = "";
	@ParamField(name = "tianHaiKey")
	public static String tianHaiKey = "";
	private final static Map<String, String> kvs = new HashMap<String, String>();

	public static synchronized Map<String, String> splitWebsite() {
		if (kvs.isEmpty()) {
			if (website != null && !website.isEmpty()) {
				String[] pairs = website.split(";");
				for (String pair : pairs) {
					String[] kv = pair.split("->");
					kvs.put(kv[0], kv[1]);
				}
			}
		}
		return kvs;
	}

	public static String replaceWebsite(String pwebsite) {
		Iterator<Entry<String, String>> it = kvs.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> pair = it.next();
			pwebsite = pwebsite.replaceAll(pair.getKey(), pair.getValue());
		}
		return pwebsite;
	}

	public static void main(String[] args) {
		website = "djtest.i12371.cn->172.16.19.100;123->456";
		splitWebsite();
		System.out.println(replaceWebsite("http://djtest.i12371.cn/party-weixintestct"));
	}
}
