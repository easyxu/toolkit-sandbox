package com.phoenix.base.spring;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class ThreadScope implements Scope {
	Logger logger = LoggerFactory.getLogger(ThreadScope.class);
	private ThreadLocal<Map<String, Object>> threadScope = new ThreadLocal<Map<String, Object>>() {
		protected Map<String, Object> initialValue() {
			return new HashMap<String, Object>();
		}
	};

	@Override
	public Object get(String name, ObjectFactory objectFactory) {
		Map<String, Object> scope = threadScope.get();
		Object object = scope.get(name);
		if (object == null) {
			object = objectFactory.getObject();
			scope.put(name, object);
		}
		return object;
	}

	public Object remove(String name) {
		Map<String, Object> scope = threadScope.get();
		return scope.remove(name);
	}

	public void registerDestructionCallback(String name, Runnable callback) {
		logger.warn("SimpleThreadScope does not support descruction callbacks. "
				+ "Consider using a RequestScope in a Web environment.");
	}

	public String getConversationId() {
		return null;
	}

//	@Override
	public Object resolveContextualObject(String key) {
		return null;
	}
}
