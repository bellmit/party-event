package com.sunsharing.party.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the T_EM_USERDEP database table.
 * 
 */
@Entity
@Table(name="T_EM_USERDEP")
public class TEmUserdep implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6347273209146520420L;

	@Id
	private String id;

	@Column(name="DEP_ID")
	private String depId;

	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="DEP_POSITION")
	private String depPosition;

	public TEmUserdep() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDepId() {
		return this.depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDepPosition() {
		return depPosition;
	}

	public void setDepPosition(String depPosition) {
		this.depPosition = depPosition;
	}

}