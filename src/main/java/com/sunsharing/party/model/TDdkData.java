package com.sunsharing.party.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the T_DDK_DATA database table.
 * 
 */
@Entity
@Table(name="T_DDK_DATA")
public class TDdkData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1365419025716578775L;

	@Id
	@Column(name="DATA_CODE")
	private String dataCode;

	@Column(name="DATA_DESC")
	private String dataDesc;

	@Column(name="DATA_NAME")
	private String dataName;

	@Column(name="PARENT_CODE")
	private String parentCode;

	@Column(name="SORT_ORDER")
	private String sortOrder;

	private String status;
	@Transient
	private String conCode;

	public TDdkData() {
	}

	public String getDataCode() {
		return this.dataCode;
	}

	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}

	public String getDataDesc() {
		return this.dataDesc;
	}

	public void setDataDesc(String dataDesc) {
		this.dataDesc = dataDesc;
	}

	public String getDataName() {
		return this.dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public String getParentCode() {
		return this.parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getConCode() {
		return conCode;
	}

	public void setConCode(String conCode) {
		this.conCode = conCode;
	}

}