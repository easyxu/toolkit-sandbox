/**
 * 
 */
package com.phoenix.common.logging;

import java.util.Stack;
/**
 * TODO
 * @author xiang.xu
 * @时间 2011-5-6 上午01:27:35
 */
public abstract interface NestedDiagnosticContext
{
  public abstract void clearNDC();

  public abstract Stack cloneNDCStack();

  public abstract String getNDC();

  public abstract int getNDCDepth();

  public abstract void inheritNDC(Stack paramStack);

  public abstract void popNDC();

  public abstract void pushNDC(String paramString);

  public abstract void removeNDC();

  public abstract void setNDCMaxDepth(int paramInt);
}
