package com.sunsharing.party.common.model.ws.enumbean;

public enum ServerEnum {

	Notice("notice","NoticeServiceImpl"),//公告通知
	Test("test","TestServiceImpl"),//测试
	Axis2("air","ProxyAxis2"),//调用air-server
	Sdk("sdk","ProxySDK");//调用协同平台
	
	private String code;
	private String value;
	
	private ServerEnum(String code,String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * 获取class名称
	 * @param code
	 * @return
	 */
	public static String getEnumValue(String code)
	{
		for(ServerEnum enum1 : ServerEnum.values()){
			if(enum1.getCode().equals(code))
			{
				return enum1.getValue();
			}
        }
		return "";
	}
	public static void main(String[] args) {
		System.out.println(ServerEnum.getEnumValue("test"));
	}
}
