package com.sunsharing.party.util;

import java.io.Serializable;

import com.sunsharing.memCache.Cache;

public class CacheUtil extends Cache {
	
	public static Object getCache(String key) {
		return get(key);
	}
	
	public static void saveCache(String key, Serializable value) {
		put(key, value);
	}
	
	public static void saveCacheByPriod(String key, Serializable value, String priod) {
		put(key, value, priod);
	}
	public static void removeCache(String key) {
		remove(key);
	}
}
