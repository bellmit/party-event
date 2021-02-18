/*
 * @(#) WebMvcConfigure
 * 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 *
 * <br> Copyright:  Copyright (c) 2017
 * <br> Company:厦门畅享信息技术有限公司
 * <br> @author sleepan
 * <br> 2017-09-29 10:29:44
 *
 */

package com.sunsharing.party;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 *
 * @author 吴宸勖
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = WebMvcConfigure.class)
public class WebMvcConfigure extends WebMvcConfigurerAdapter {

    public WebMvcConfigure() {
        System.out.println("WebMvcConfigure created");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.extendMessageConverters(converters);
    }

}
