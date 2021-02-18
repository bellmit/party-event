package com.sunsharing.party.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_EM_SMSCONF")
public class TEmSMSConf {
	
	@Id
	@Column(name = "CONF_ID")
	private String confId;
	@Column(name = "TYPE_ID")
	private String typeId;
	@Column(name = "CONF_DESC")
	private String confDesc;
	@Column(name = "CONF_CONTEXT")
	private String confConText;
	@Column(name = "CREATE_TIME")
	private String createTime;
	@Column(name = "STATUS")
	private String status;
	
	public TEmSMSConf() {
		
	}
	
	public String getConfId() {
		return confId;
	}
	
	public void setConfId(String confId) {
		this.confId = confId;
	}
	
	public String getTypeId() {
		return typeId;
	}
	
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	public String getConfDesc() {
		return confDesc;
	}
	
	public void setConfDesc(String confDesc) {
		this.confDesc = confDesc;
	}
	
	public String getConfConText() {
		return confConText;
	}
	
	public void setConfConText(String confConText) {
		this.confConText = confConText;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
}
