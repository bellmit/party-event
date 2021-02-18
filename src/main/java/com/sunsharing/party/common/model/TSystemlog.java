package com.sunsharing.party.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * The persistent class for the T_SYSTEMLOG database table.
 * 
 */
@Entity
@Table(name = "T_SYSTEMLOG")
public class TSystemlog implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7669011183558726484L;
	
	@Id
	@Column(name = "ID")
	private String id;
	
	@Column(name = "CREATE_TIME")
	private String createTime;
	
	@Column(name = "IP")
	private String ip;
	
	@Lob
	@Column(name = "LOG_DESC")
	private String logDesc;
	
	@Column(name = "LOG_TYPE")
	private String logType;
	
	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name = "SERVICE_ID")
	private String serviceId;
	
	@Column(name = "METHOD_NAME")
	private String methodName;
	
	@Column(name = "USER_NAME")
	private String userName;
	
	@Column(name = "SYSTEM_TYPE")
	private String systemType;
	
	@Column(name = "SORT_NUM")
	private int sortNum;
	
	@Column(name = "PARAMS")
	private boolean params;
	
	@Column(name = "RUN_TIME")
	private long runTime;
	
	@Column(name = "SERVICE_DESC")
	private String serviceDesc;
	
	@Column(name = "METHOD_DESC")
	private String methodDesc;
	
	public TSystemlog() {
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getCreateTime() {
		return this.createTime;
	}
	
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getIp() {
		return this.ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getLogDesc() {
		return this.logDesc;
	}
	
	public void setLogDesc(String logDesc) {
		this.logDesc = logDesc;
	}
	
	public String getLogType() {
		return this.logType;
	}
	
	public void setLogType(String logType) {
		this.logType = logType;
	}
	
	public String getUserId() {
		return this.userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getServiceId() {
		return serviceId;
	}
	
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public boolean isParams() {
		return params;
	}
	
	public void setParams(boolean params) {
		this.params = params;
	}
	
	public long getRunTime() {
		return runTime;
	}
	
	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public int getSortNum() {
		return sortNum;
	}
	
	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}
	
	public String getSystemType() {
		return systemType;
	}
	
	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}
	
	public String getServiceDesc() {
		return serviceDesc;
	}
	
	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	
	public String getMethodDesc() {
		return methodDesc;
	}
	
	public void setMethodDesc(String methodDesc) {
		this.methodDesc = methodDesc;
	}
	
}
