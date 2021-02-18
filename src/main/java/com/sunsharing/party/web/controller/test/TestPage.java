package com.sunsharing.party.web.controller.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @RestController注解相当于@ResponseBody ＋ @Controller合在一起的作用。 到达页面
 *                                   如果为@RestController 不能到达页面但是@ShareRest
 *                                   是基于@RestController之上所以建议到达页面和获取数据分开
 *                                   <p>
 *                                   </p>
 * @author wxl 2017年11月30日 下午4:32:55
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年11月30日
 * @modify by reason:{方法名}:{原因}
 */
@Controller
public class TestPage {

	@RequestMapping(value = "goTest.do")
	public String goTest() {
		return "test";
	}

}
