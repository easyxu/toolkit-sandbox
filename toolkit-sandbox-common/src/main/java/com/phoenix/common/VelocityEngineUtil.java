package com.phoenix.common;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.MethodInvocationException;
import org.springframework.beans.factory.InitializingBean;

public class VelocityEngineUtil implements InitializingBean {
	static Logger log = Logger.getLogger(VelocityEngineUtil.class);
	private static final String DEFAULT_ENCODING = "UTF-8";
	private Map<String, String> properties;
	private VelocityEngine velocityEngine;

	public void afterPropertiesSet() throws Exception {
		velocityEngine = new VelocityEngine();
		Set<Entry<String, String>> entrySet = properties.entrySet();
		Iterator<Entry<String, String>> itr = entrySet.iterator();
		while (itr.hasNext()) {
			Entry<String, String> entry = itr.next();
			String key = entry.getKey();
			String value = entry.getValue();
			velocityEngine.setProperty(key, value);
		}
	}

	@SuppressWarnings("unchecked")
	public String mergeTemplate(String templateName, Map model, String encoding) {
		StringWriter writer = new StringWriter();
		VelocityContext vc = new VelocityContext(model);
		try {
			velocityEngine.mergeTemplate(templateName,
					encoding == null ? DEFAULT_ENCODING : encoding, vc, writer);
		} catch (ResourceNotFoundException e) {
			log.error(e);
		} catch (ParseErrorException e) {
			log.error(e);
		} catch (MethodInvocationException e) {
			log.error(e);
		} catch (Exception e) {
			log.error(e);
		}
		return writer.toString();

	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
