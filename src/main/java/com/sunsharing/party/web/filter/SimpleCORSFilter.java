package com.sunsharing.party.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 允许跨域的过滤器
 * <p>
 * </p>
 * @author wxl 2017年11月23日 上午11:58:16
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年11月23日
 * @modify by reason:{方法名}:{原因}
 */
public class SimpleCORSFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws IOException, ServletException {
		final HttpServletResponse res = (HttpServletResponse)response;
		final HttpServletRequest req = (HttpServletRequest)request;
		// res.setHeader("Access-Control-Allow-Origin", "*");
		// 服务器端 Access-Control-Allow-Credentials = true时，参数Access-Control-Allow-Origin 的值不能为 '*'
		res.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
		res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		res.setHeader("Access-Control-Max-Age", "3600");
		res.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		// 运行客户端携带证书式访问
		res.setHeader("Access-Control-Allow-Credentials", "true");
		chain.doFilter(request, response);
	}
	
	@Override
	public void destroy() {
		
	}
	
}
