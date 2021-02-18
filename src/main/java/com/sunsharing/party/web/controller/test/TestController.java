/*
 * @(#) TestController 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究 <br> Copyright: Copyright (c) 2017 <br> Company:厦门畅享信息技术有限公司 <br> @author sleepan <br> 2017-09-28 17:36:57
 */

package com.sunsharing.party.web.controller.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunsharing.common.utils.StringUtils;
import com.sunsharing.ihome.air.common.anno.AirLogAnnotation;
import com.sunsharing.ihome.air.common.util.ResultTypeEnum;
import com.sunsharing.ihome.air.common.ws.AirClientContext;
import com.sunsharing.party.common.api.Test;
import com.sunsharing.party.entity.query.Pagination;
import com.sunsharing.party.web.controller.BaseController;
import com.sunsharing.share.webex.annotation.ShareRest;
import com.sunsharing.zeus.sso.session.SessionUser;

/**
 * @RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用。 如果为@RestController
 *                                   不能到达页面 但是@ShareRest
 *                                   是基于@RestController之上所以建议到达页面和获取数据分开
 *                                   <p>
 *                                   </p>
 * @author wxl 2017年11月30日 下午4:33:31
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年11月30日
 * @modify by reason:{方法名}:{原因}
 */
@ShareRest
@RestController
public class TestController extends BaseController {

	private static final Logger logger = LogManager.getLogger(TestController.class);

	@GetMapping(value = "/test.do")
	@AirLogAnnotation(logDesc = "测试", logKey = "/test.do", logType = ResultTypeEnum.web)
	public List test() {
		final SessionUser sessionUser = SessionUser.load();
		logger.info("获取登录账号：" + sessionUser.getLastLogin());
		logger.info("test requested");
		final Map<String, String> data = new HashMap<String, String>();
		final Date bir = new Date();
		data.put("userId", "001");
		data.put("userName", "test");
		data.put("sex", "女");
		final List test = new ArrayList();
		test.add(data);
		return test;
	}

	@RequestMapping(value = "/getTestData.do")
	public Pagination getTestData1(HttpServletRequest request) {
		/*
		 * final WeiXinApi wx = WeiXinFactory.init("QYH"); final String ticket =
		 * wx.getTicket("wx146410cb95281433",
		 * "1gR9KE1GwBn_2r4drKcrNpvJebuNJIiUCQf6Dw5tCYCnaGRqZjp8U_5AlqIkYYB8");
		 */
		final Test test = AirClientContext.getBean(Test.class);
		final String s = request.getQueryString();
		final String username = request.getParameter("username");
		final String userid = request.getParameter("userid");
		final String pageNo = request.getParameter("currentPage");
		final String pageSize = request.getParameter("linesPerPage");
		final String sex = request.getParameter("sex");
		final Map<String, Object> param = new HashMap<>();
		param.put("username", username);
		param.put("sex", sex);
		param.put("userid", userid);
		param.put("pageNo", StringUtils.isBlank(pageNo) ? "1" : pageNo);
		param.put("pageSize", StringUtils.isBlank(pageSize) ? "10" : pageSize);
		final Pagination result = test.getTestData(param);
		// throw new NullPointerException();
		return result;

	}

}
