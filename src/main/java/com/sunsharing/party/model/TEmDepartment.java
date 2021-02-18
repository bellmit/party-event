package com.sunsharing.party.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the T_EM_DEPARTMENT database table.
 * 
 */
@Entity
@Table(name="T_EM_DEPARTMENT")
public class TEmDepartment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6808424512324782059L;

	@Id
	@Column(name="DEP_ID")
	private String depId;

	@Column(name="DEP_LEVEL")
	private String depLevel;

	@Column(name="DEP_NAME")
	private String depName;

	@Column(name="DEP_PARENT")
	private String depParent;
	
	@Column(name="DEP_STATUS")
	private String depstatus;

	public TEmDepartment() {
	}

	public String getDepId() {
		return this.depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	public String getDepLevel() {
		return this.depLevel;
	}

	public void setDepLevel(String depLevel) {
		this.depLevel = depLevel;
	}

	public String getDepName() {
		return this.depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getDepParent() {
		return this.depParent;
	}

	public void setDepParent(String depParent) {
		this.depParent = depParent;
	}

	public String getDepstatus() {
		return depstatus;
	}

	public void setDepstatus(String depstatus) {
		this.depstatus = depstatus;
	}

	
}