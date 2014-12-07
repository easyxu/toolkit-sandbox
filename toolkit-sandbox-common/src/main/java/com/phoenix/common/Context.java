/**
 * 
 */
package com.phoenix.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuxiang<xiang.leau@gmail.com>
 * @date 2011-8-18 下午04:27:27
 */
public abstract class Context {

	private static Map<String,Object> map = Collections.synchronizedMap(new HashMap<String,Object>());
	
	public static Object get(String key){
		return map.get(key);
	}
	public static void put(String key,Object value){
		map.put(key, value);
	}
	
	public static void clear(){
		map.clear();
	}
	public static boolean containsKey(String key){
		return map.containsKey(key);
	}
}
