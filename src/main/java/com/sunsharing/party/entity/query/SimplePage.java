package com.sunsharing.party.entity.query;

/**
 * 简单分页类
 */
public class SimplePage implements Paginable {
	private static final long serialVersionUID = 1L;
	public static final int DEF_COUNT = 20;

	/**
	 * 检查页码 checkcurrentPage
	 * 
	 * @param currentPage
	 * @return if currentPage==null or currentPage<1 then return 1 else return currentPage
	 */
	public static int cpn(Integer currentPage) {
		return (currentPage == null || currentPage < 1) ? 1 : currentPage;
	}

	public SimplePage() {
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
	public SimplePage(int currentPage, int linesPerPage, int totalNum) {
		setTotalNum(totalNum);
		setLinesPerPage(linesPerPage);
		setCurrentPage(currentPage);
		adjustCurrentPage();
	}

	/**
	 * 调整页码，使不超过最大页数
	 */
	public void adjustCurrentPage() {
		if (currentPage == 1) {
			return;
		}
		int tp = getTotalPage();
		if (currentPage > tp) {
			currentPage = tp;
		}
	}

	/**
	 * 获得页码
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * 每页几条数据
	 */
	public int getLinesPerPage() {
		return linesPerPage;
	}

	/**
	 * 总共几条数据
	 */
	public int getTotalNum() {
		return totalNum;
	}

	/**
	 * 总共几页
	 */
	public int getTotalPage() {
		int totalPage = totalNum / linesPerPage;
		if (totalPage == 0 || totalNum % linesPerPage != 0) {
			totalPage++;
		}
		return totalPage;
	}

	/**
	 * 是否第一页
	 */
	public boolean isFirstPage() {
		return currentPage <= 1;
	}

	/**
	 * 是否最后一页
	 */
	public boolean isLastPage() {
		return currentPage >= getTotalPage();
	}

	/**
	 * 下一页页码
	 */
	public int getNextPage() {
		if (isLastPage()) {
			return currentPage;
		} else {
			return currentPage + 1;
		}
	}

	/**
	 * 上一页页码
	 */
	public int getPrePage() {
		if (isFirstPage()) {
			return currentPage;
		} else {
			return currentPage - 1;
		}
	}

	protected int totalNum = 0;
	protected int linesPerPage = 20;
	protected int currentPage = 1;

	/**
	 * if totalNum<0 then totalNum=0
	 * 
	 * @param totalNum
	 */
	public void setTotalNum(int totalNum) {
		if (totalNum < 0) {
			this.totalNum = 0;
		} else {
			this.totalNum = totalNum;
		}
	}

	/**
	 * if linesPerPage< 1 then linesPerPage=DEF_COUNT
	 * 
	 * @param linesPerPage
	 */
	public void setLinesPerPage(int linesPerPage) {
		if (linesPerPage < 1) {
			this.linesPerPage = DEF_COUNT;
		} else {
			this.linesPerPage = linesPerPage;
		}
	}

	/**
	 * if currentPage < 1 then currentPage=1
	 * 
	 * @param currentPage
	 */
	public void setCurrentPage(int currentPage) {
		if (currentPage < 1) {
			this.currentPage = 1;
		} else {
			this.currentPage = currentPage;
		}
	}
}
