/**
 * 
 */
package com.phoenix.common.logging;

import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
/**
 * TODO
 * @author xiang.xu
 * @时间 2011-5-6 上午01:26:29
 */
public abstract interface Logger extends Log{
	public abstract void trace(Object paramObject, Object[] paramArrayOfObject);

	  public abstract void trace(Object paramObject, Object[] paramArrayOfObject, Throwable paramThrowable);

	  public abstract void debug(Object paramObject, Object[] paramArrayOfObject);

	  public abstract void debug(Object paramObject, Object[] paramArrayOfObject, Throwable paramThrowable);

	  public abstract void info(Object paramObject, Object[] paramArrayOfObject);

	  public abstract void info(Object paramObject, Object[] paramArrayOfObject, Throwable paramThrowable);

	  public abstract void warn(Object paramObject, Object[] paramArrayOfObject);

	  public abstract void warn(Object paramObject, Object[] paramArrayOfObject, Throwable paramThrowable);

	  public abstract void error(Object paramObject, Object[] paramArrayOfObject);

	  public abstract void error(Object paramObject, Object[] paramArrayOfObject, Throwable paramThrowable);

	  public abstract void fatal(Object paramObject, Object[] paramArrayOfObject);

	  public abstract void fatal(Object paramObject, Object[] paramArrayOfObject, Throwable paramThrowable);

	  public abstract ResourceBundle getResourceBundle();

	  public abstract void setResourceBundle(ResourceBundle paramResourceBundle);

	  public abstract String toString();

	  public abstract NestedDiagnosticContext getNestedDiagnosticContext();

	  public abstract MappedDiagnosticContext getMappedDiagnosticContext();
}
