/*
 * @(#) ShareWebExConfigure2 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究 <br> Copyright: Copyright (c) 2017 <br> Company:厦门畅享信息技术有限公司 <br> @author sleepan <br> 2017-09-29
 * 10:32:05
 */

package com.sunsharing.party;

import org.springframework.context.annotation.Configuration;

import com.sunsharing.share.webex.ShareWebExConfigure;
import com.sunsharing.share.webex.annotation.EnableShareAdvice;

/**
 * Created by wucx on 2017/9/21.
 */
// 不能直接加载EnableWebMvc下，会无效
@Configuration
@EnableShareAdvice
public class MyShareWebExConfigure extends ShareWebExConfigure {
	
	@Override
	public boolean isDev() {
		// 返回false异常信息不会在浏览器显示正式环境建议设置为false
		return false;
	}
}
