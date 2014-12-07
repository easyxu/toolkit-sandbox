/**
 * 
 */
package com.phoenix.common.logging.spi;

import java.util.ResourceBundle;
import org.apache.commons.logging.Log;

import com.phoenix.common.logging.Logger;
import com.phoenix.common.logging.MappedDiagnosticContext;
import com.phoenix.common.logging.NestedDiagnosticContext;
/**
 * TODO
 * @author xiang.xu
 * @时间 2011-5-6 上午01:28:38
 */
public class LoggerWrapper implements Logger{

	private Log log;
	  private ResourceBundle bundle;

	  public LoggerWrapper(Log log)
	  {
	    this.log = log;
	  }

	  public boolean isTraceEnabled()
	  {
	    return this.log.isTraceEnabled();
	  }

	  public boolean isDebugEnabled()
	  {
	    return this.log.isDebugEnabled();
	  }

	  public boolean isInfoEnabled()
	  {
	    return this.log.isInfoEnabled();
	  }

	  public boolean isWarnEnabled()
	  {
	    return this.log.isWarnEnabled();
	  }

	  public boolean isErrorEnabled()
	  {
	    return this.log.isErrorEnabled();
	  }

	  public boolean isFatalEnabled()
	  {
	    return this.log.isFatalEnabled();
	  }

	  public void trace(Object message)
	  {
	    this.log.trace(message);
	  }

	  public void trace(Object message, Throwable cause)
	  {
	    this.log.trace(message, cause);
	  }

	  public void trace(Object key, Object[] params)
	  {
	    if (this.log.isTraceEnabled())
	      this.log.trace(LoggerUtil.getMessage(this, key, params));
	  }

	  public void trace(Object key, Object[] params, Throwable cause)
	  {
	    if (this.log.isTraceEnabled())
	      this.log.trace(LoggerUtil.getMessage(this, key, params), cause);
	  }

	  public void debug(Object message)
	  {
	    this.log.debug(message);
	  }

	  public void debug(Object message, Throwable cause)
	  {
	    this.log.debug(message, cause);
	  }

	  public void debug(Object key, Object[] params)
	  {
	    if (this.log.isDebugEnabled())
	      this.log.debug(LoggerUtil.getMessage(this, key, params));
	  }

	  public void debug(Object key, Object[] params, Throwable cause)
	  {
	    if (this.log.isDebugEnabled())
	      this.log.debug(LoggerUtil.getMessage(this, key, params), cause);
	  }

	  public void info(Object message)
	  {
	    this.log.info(message);
	  }

	  public void info(Object message, Throwable cause)
	  {
	    this.log.info(message, cause);
	  }

	  public void info(Object key, Object[] params)
	  {
	    if (this.log.isInfoEnabled())
	      this.log.info(LoggerUtil.getMessage(this, key, params));
	  }

	  public void info(Object key, Object[] params, Throwable cause)
	  {
	    if (this.log.isInfoEnabled())
	      this.log.info(LoggerUtil.getMessage(this, key, params), cause);
	  }

	  public void warn(Object message)
	  {
	    this.log.warn(message);
	  }

	  public void warn(Object message, Throwable cause)
	  {
	    this.log.warn(message, cause);
	  }

	  public void warn(Object key, Object[] params)
	  {
	    if (this.log.isWarnEnabled())
	      this.log.warn(LoggerUtil.getMessage(this, key, params));
	  }

	  public void warn(Object key, Object[] params, Throwable cause)
	  {
	    if (this.log.isWarnEnabled())
	      this.log.warn(LoggerUtil.getMessage(this, key, params), cause);
	  }

	  public void error(Object message)
	  {
	    this.log.error(message);
	  }

	  public void error(Object message, Throwable cause)
	  {
	    this.log.error(message, cause);
	  }

	  public void error(Object key, Object[] params)
	  {
	    if (this.log.isErrorEnabled())
	      this.log.error(LoggerUtil.getMessage(this, key, params));
	  }

	  public void error(Object key, Object[] params, Throwable cause)
	  {
	    if (this.log.isErrorEnabled())
	      this.log.error(LoggerUtil.getMessage(this, key, params), cause);
	  }

	  public void fatal(Object message)
	  {
	    this.log.fatal(message);
	  }

	  public void fatal(Object message, Throwable cause)
	  {
	    this.log.fatal(message, cause);
	  }

	  public void fatal(Object key, Object[] params)
	  {
	    if (this.log.isFatalEnabled())
	      this.log.fatal(LoggerUtil.getMessage(this, key, params));
	  }

	  public void fatal(Object key, Object[] params, Throwable cause)
	  {
	    if (this.log.isFatalEnabled())
	      this.log.fatal(LoggerUtil.getMessage(this, key, params), cause);
	  }

	  public ResourceBundle getResourceBundle()
	  {
	    return this.bundle;
	  }

	  public void setResourceBundle(ResourceBundle bundle)
	  {
	    this.bundle = bundle;
	  }

	  public String toString()
	  {
	    return this.log.toString();
	  }

	  public NestedDiagnosticContext getNestedDiagnosticContext()
	  {
	    return NoopDiagnosticContext.getInstance();
	  }

	  public MappedDiagnosticContext getMappedDiagnosticContext()
	  {
	    return NoopDiagnosticContext.getInstance();
	  }
}
