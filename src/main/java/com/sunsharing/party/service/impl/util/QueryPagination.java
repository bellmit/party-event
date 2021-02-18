package com.sunsharing.party.service.impl.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.sunsharing.party.entity.query.Pagination;


public class QueryPagination {

	private Logger logger = Logger.getLogger(QueryPagination.class);

	/**
	 * 查询分页方法
	 * @param sql 
	 * @param parList 入参list
	 * @param pageNo
	 * @param pageSize
	 * @param jdbcTemplate
	 * @return
	 */
	@Transactional(readOnly = true)
	public Pagination queryList(String sql, List<Object> parList, int pageNo,
			int pageSize, JdbcTemplate jdbcTemplate)
	{
		return queryList(sql, parList, pageNo, pageSize, null, jdbcTemplate);
	}
	@Transactional(readOnly = true)
	public Pagination queryList(String sql, List<Object> parList, int pageNo,
			int pageSize,Class className, JdbcTemplate jdbcTemplate) {
		Pagination page = new Pagination();
		String sqlCount = "SELECT count(1) FROM (" + sql + ") t";
		logger.info("countSql=========:" + sqlCount + "," + parList.toString());
		int count = jdbcTemplate.queryForObject(sqlCount, parList.toArray(),Integer.class);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int start = pageNo;
		int end = pageSize;
		if (count > 0) {
			parList.add((start - 1) * end + "");
			parList.add(start * end + "");
			String sqls = "SELECT * FROM (SELECT rownum RN,t.* FROM (" + sql
					+ ") t ) where RN>? and RN<=?";
			logger.info("querySql:" + sql + "," + parList.toString());
			if(className!=null){
				List lists = jdbcTemplate.query(sqls,parList.toArray(),new BeanPropertyRowMapper(className));
				page.setList(lists);
			}else{
				list = jdbcTemplate.queryForList(sqls, parList.toArray());
				page.setList(list);
			}
		}else{
			page.setList(list);
		}
		
		page.setTotalNum(count);
		page.setCurrentPage(start);
		page.setLinesPerPage(end);
		return page;
	}
}
