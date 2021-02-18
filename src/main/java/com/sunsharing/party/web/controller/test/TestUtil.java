package com.sunsharing.party.web.controller.test;

import org.apache.commons.lang3.StringUtils;

import com.sunsharing.share.common.text.TextValidator;

public class TestUtil {
	
	public void Test() {
		// 优先使用 org.apache.commons.lang3
		StringUtils.equals(null, null);
		StringUtils.equals(null, "abc");
		StringUtils.equals("abc", null);
		StringUtils.equals("abc", "abc");
		StringUtils.equals("abc", "ABC");
		
		StringUtils.equalsIgnoreCase("abc", "ABC");
		
		StringUtils.compare(null, null);
		StringUtils.compare(null, "a");
		StringUtils.compare("a", null);
		StringUtils.compare("abc", "abc");
		StringUtils.compare("a", "b");
		StringUtils.compare("b", "a");
		StringUtils.compare("a", "B");
		StringUtils.compare("ab", "abc");
		
		StringUtils.compareIgnoreCase("abc", "ABC");
		
		StringUtils.equalsAny("abc", "abc", "def");
		StringUtils.equalsAny("abc", "ABC", "DEF");
		
		StringUtils.equalsAnyIgnoreCase("abc", "abc", "def");
		StringUtils.equalsAnyIgnoreCase("abc", "ABC", "DEF");
	}
	
	public static void main(String[] args) {
		// 验证手机号 1字头＋10位数字
		TextValidator.isMobileSimple("1500099998");
		// 验证手机号（精确), 已知3位前缀＋8位数字
		TextValidator.isMobileExact("1500099998");// true
		TextValidator.isMobileExact("1400099998");// false
		
		// 验证固定电话号码 可带区号，然后6至少8位数字
		TextValidator.isTel("88990098"); // true
		TextValidator.isTel("889930098"); // false
		TextValidator.isTel("010-88990098"); // true
		TextValidator.isTel("01-88990098"); // false
		
		// 验证邮箱, 有效字符(不支持中文), 且中间必须有@，后半部分必须有.
		TextValidator.isEmail("abc@a"); // false
		TextValidator.isEmail("中文@a.com");// false
		TextValidator.isEmail("abc@abc.com");// true
		
		// 验证URL, 必须有"://",前面必须是英文，后面不能有空格
		TextValidator.isUrl("abc.com");// false
		TextValidator.isUrl("http://abc.c om");// false
		TextValidator.isUrl("http2://abc.com");// false
		TextValidator.isUrl("http://abc.com");// true
		
		// 验证IP地址
		TextValidator.isIp("192.168.0.1");// true
		
		// 验证 yyyy-MM-dd 的日期格式的验证，已考虑平闰年
		TextValidator.isDate("2011-02-29");// false
		TextValidator.isDate("2012-02-29");// true
		
		// 验证 yyyy-MM-dd HH:mm:ss 的日期格式的验证，已考虑平闰年
		TextValidator.isDatetime("2011-02-28 00:60:59");// false
		TextValidator.isDatetime("2012-02-29 00:50:59");// true
		
		// 验证标准12位行政区划
		TextValidator.isRegionNo("350521000"); // false
		TextValidator.isRegionNo("950521000000");// false
		TextValidator.isRegionNo("350521000000");// true
		
		// 验证身份证
		// simple 模式仅验证正则
		// 验证 15位和18位的身份证号
		TextValidator.isIdCardSimple("440101198909204518"); // true
		// 仅验证 18位的身份证号
		TextValidator.isIdCard18Simple("440101198909204518");// true
		
		// Exact 模式为准确验证模式
		// 验证 15位和18位的身份证号
		TextValidator.isIdCardExact("440101198909204518");// false
		// 仅验证 18位的身份证号
		TextValidator.isIdCard18Exact("440101198909204518");// false
		
		// simple 模式仅验证正则
		// 验证 15位和18位的身份证号
		TextValidator.isIdCardSimple("440101198909204518"); // true
		// 仅验证 18位的身份证号
		TextValidator.isIdCard18Simple("440101198909204518");// true
		
		// Exact 模式为准确验证模式
		// 验证 15位和18位的身份证号
		TextValidator.isIdCardExact("440101198909204518");// false
		// 仅验证 18位的身份证号
		TextValidator.isIdCard18Exact("440101198909204518");// false
		
	}
}
