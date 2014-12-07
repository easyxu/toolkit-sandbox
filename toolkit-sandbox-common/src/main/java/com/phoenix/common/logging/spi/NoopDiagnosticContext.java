/**
 * 
 */
package com.phoenix.common.logging.spi;

import com.phoenix.common.logging.MappedDiagnosticContext;
import com.phoenix.common.logging.NestedDiagnosticContext;
import java.util.Map;
import java.util.Stack;

/**
 * TODO
 * 
 * @author xiang.xu
 * @时间 2011-5-6 上午01:30:34
 */
public class NoopDiagnosticContext implements NestedDiagnosticContext,
		MappedDiagnosticContext {

	private static final NoopDiagnosticContext instance = new NoopDiagnosticContext();

	public static NoopDiagnosticContext getInstance() {
		return instance;
	}

	public void clearNDC() {
	}

	public Stack cloneNDCStack() {
		return new Stack();
	}

	public String getNDC() {
		return null;
	}

	public int getNDCDepth() {
		return 0;
	}

	public void inheritNDC(Stack stack) {
	}

	public void popNDC() {
	}

	public void pushNDC(String s) {
	}

	public void removeNDC() {
	}

	public void setNDCMaxDepth(int i) {
	}

	public void putMDC(String key, Object obj) {
	}

	public Object getMDC(String key) {
		return null;
	}

	public void removeMDC(String key) {
	}

	public Map getMDCContext() {
		return null;
	}
}
