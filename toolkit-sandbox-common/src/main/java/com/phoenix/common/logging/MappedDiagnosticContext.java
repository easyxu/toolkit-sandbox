/**
 * 
 */
package com.phoenix.common.logging;

import java.util.Map;

/**
 * TODO
 * 
 * @author xiang.xu
 * @时间 2011-5-6 上午01:25:57
 */
public interface MappedDiagnosticContext {

	public abstract void putMDC(String paramString, Object paramObject);

	public abstract Object getMDC(String paramString);

	public abstract void removeMDC(String paramString);

	public abstract Map getMDCContext();
}
