/**
 * 
 */
package com.phoenix.common.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.phoenix.common.logging.spi.LoggerWrapper;

/**
 * TODO
 * @author xiang.xu
 * @时间 2011-5-6 上午01:28:04
 */
public abstract class LoggerFactory extends LogFactory
{
  public static Logger getLogger(Class clazz)
  {
    return getLogger(getLog(clazz));
  }

  public static Logger getLogger(String name)
  {
    return getLogger(getLog(name));
  }

  private static Logger getLogger(Log log)
  {
    if ((log instanceof Logger)) {
      return (Logger)log;
    }

    return new LoggerWrapper(log);
  }
}