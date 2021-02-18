package com.sunsharing.party.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * tonken获取及其生成操作类
 * 
 * @author hanjianbo 2017年9月5日 下午5:34:10
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年9月5日
 * @modify by reason:{方法名}:{原因}
 */
@Controller
@RequestMapping(value = "/event")
public class TokenController extends BaseController {

	static Logger logger = Logger.getLogger(TokenController.class);

	/**
	 * 获取用户的token
	 * 
	 * @author lixinqiao 2017年8月9日 下午3:06:58
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "getToken.do")
	@ResponseBody
	public String getToken(HttpServletRequest request, HttpServletResponse response) {
		final String fxToken = getUserToken(request, response);
		logger.info("获取到的token为:" + fxToken);
		return fxToken;
	}

	/**
	 * 用户无权限跳转的链接
	 * 
	 * @author lixinqiao 2017年8月10日 上午9:52:09
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "noPermission.do")
	public String noPermission(Model model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("msg", "您不具备访问该页面的权限！");
		return "errors/nopermission";
	}
}
