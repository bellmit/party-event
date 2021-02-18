package com.sunsharing.party.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the T_DDK_MENU database table.
 * 
 */
@Entity
@Table(name="T_DDK_MENU")
public class TDdkMenu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3087331344127022523L;

	@Id
	@Column(name="MENU_CODE")
	private String menuCode;

	@Column(name="MENU_CSS")
	private String menuCss;

	@Column(name="CHILD_CSS")
	private String childCss;
	@Column(name="CHILD_BGCSS")
	private String childBgcss;
	
	@Column(name="MENU_DESC")
	private String menuDesc;

	@Column(name="MENU_NAME")
	private String menuName;

	@Column(name="MENU_URL")
	private String menuUrl;
	
	@Column(name="INNER_MENU_URL")
	private String innerMenuUrl;

	@Column(name="PARENT_CODE")
	private String parentCode;

	@Column(name="SORT_ORDER")
	private String sortOrder;
	
	@Column(name="OPEN_TYPE")
	private String openType;

	public TDdkMenu() {
	}

	public String getMenuCode() {
		return this.menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuCss() {
		return this.menuCss;
	}

	public void setMenuCss(String menuCss) {
		this.menuCss = menuCss;
	}

	public String getMenuDesc() {
		return this.menuDesc;
	}

	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}

	public String getMenuName() {
		return this.menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return this.menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
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

	public String getChildCss() {
		return childCss;
	}

	public void setChildCss(String childCss) {
		this.childCss = childCss;
	}

	public String getChildBgcss() {
		return childBgcss;
	}

	public void setChildBgcss(String childBgcss) {
		this.childBgcss = childBgcss;
	}

	public String getInnerMenuUrl() {
		return innerMenuUrl;
	}

	public void setInnerMenuUrl(String innerMenuUrl) {
		this.innerMenuUrl = innerMenuUrl;
	}

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

}