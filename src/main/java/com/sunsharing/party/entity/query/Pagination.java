package com.sunsharing.party.entity.query;

import java.util.List;

/**
 * 列表分页。包含list属性。
 */
@SuppressWarnings("serial")
public class Pagination extends SimplePage implements java.io.Serializable,
		Paginable {

	public Pagination() {
	}

	/**
	 * 构造器
	 * 
	 * @param currentPage
	 *            页码
	 * @param linesPerPage
	 *            每页几条数据
	 * @param totalNum
	 *            总共几条数据
	 */
	public Pagination(int currentPage, int linesPerPage, int totalNum) {
		super(currentPage, linesPerPage, totalNum);
	}

	/**
	 * 构造器
	 * 
	 * @param currentPage
	 *            页码
	 * @param linesPerPage
	 *            每页几条数据
	 * @param totalNum
	 *            总共几条数据
	 * @param list
	 *            分页内容
	 */
	public Pagination(int currentPage, int linesPerPage, int totalNum, List<?> list,Page page) {
		super(currentPage, linesPerPage, totalNum);
		this.list = list;
	}

	/**
	 * 第一条数据位置
	 * 
	 * @return
	 */
	public int getFirstResult() {
		return (currentPage - 1) * linesPerPage;
	}

	/**
	 * 当前页的数据
	 */
	private List<?> list;

	/**
	 * 页码对象
	 */
	private Page page;

	/**
	 * 获得分页内容
	 * 
	 * @return
	 */
	public List<?> getList() {
		return list;
	}

	/**
	 * 设置分页内容
	 * 
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void setList(List list) {
		this.list = list;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getCountSql(String sql)
	{
		return "SELECT count(1) FROM ("+sql+") t";
	}
	public String getPaginationSql(String sql)
	{
		return "SELECT * FROM (SELECT rownum RN,t.* FROM ("+sql+") t ) where RN>? and RN<=?";
	}
}
