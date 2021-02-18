/**
 * @(#)CheckLogin 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 * 
 *                <br>
 *                Copyright: Copyright (c) 2014 <br>
 *                Company:厦门畅享信息技术有限公司 <br>
 * @author ulyn <br>
 *         14-6-10 下午2:25 <br>
 * @version 1.0 ———————————————————————————————— 修改记录 修改者： 修改时间： 修改原因： ————————————————————————————————
 */
package com.sunsharing.party.common.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sunsharing.party.common.response.ResultTypeEnum;

/**
 * <pre></pre>
 * 
 * <br>
 * ---------------------------------------------------------------------- <br>
 * <b>功能描述:</b> <br>
 * 检测登录 <br>
 * 注意事项: <br>
 * <br>
 * <br>
 * ---------------------------------------------------------------------- <br>
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckLogin {
	
	ResultTypeEnum value() default ResultTypeEnum.page;
	
	/**
	 * 验证后跳转项
	 * @author wangchuan 2016年9月13日 下午5:52:22
	 * @return
	 */
	String cValue() default "/login/goAuthorization.do";
	
	/**
	 * 微信的state的key值
	 * @author wangchuan 2017年9月6日 上午11:35:08
	 * @return
	 */
	String cCode() default "";
	
	/**
	 * 验证类型:MC-移动前端 C-门户前端 S-门户后端
	 * @author wangchuan 2017年9月5日 下午4:36:44
	 * @return
	 */
	String cType() default "MC";
	
	/**
	 * 微信类型 企业号(QYH) 或者公众号(GZH)
	 * @author wxl 2017年11月24日 下午4:10:05
	 * @return
	 */
	String weiXinType() default "QYH";
}
