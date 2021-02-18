package com.sunsharing.party.common.api;

import java.util.Map;

import com.sunsharing.component.ulynlist.model.BusinessData;
import com.sunsharing.ihome.air.common.anno.AirService;

@AirService
public interface UlynlistDbExecutor {

	BusinessData getListAndTotalCount(Map<String, String> paramMap, String listSqlKey, String dbType) throws Exception;
}
