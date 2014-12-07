/**
 * 
 */
package com.phoenix.common.logging.spi;

import org.apache.commons.logging.impl.LogFactoryImpl;
/**
 * TODO
 * @author xiang.xu
 * @时间 2011-5-6 上午01:33:01
 */
public class GenericLoggerFactory extends LogFactoryImpl{

	private static final String LOG4J_LOGGER_CLASS = "com.phoenix.common.logging.spi.log4j.Log4jLogger";
	  private static final String JDK14_LOGGER_CLASS = "com.phoenix.common.logging.spi.jdk14.JDK14Logger";

	  protected String getLogClassName()
	  {
	    String logClassName = null;

	    if (isLog4JAvailable()) {
	      return "com.alibaba.common.logging.spi.log4j.Log4jLogger";
	    }
	   
	    if (isJdk14Available()) {
	      return "com.alibaba.common.logging.spi.jdk14.JDK14Logger";
	    }

	    return super.getLogClassName();
	  }
}
