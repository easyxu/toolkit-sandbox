package com.phoenix.common.expression;

import com.phoenix.common.lang.ClassUtil;

/**
 * 抽象的<code>Expression</code>实现。
 *
 * @author Michael Zhou
 * @version $Id: ExpressionSupport.java 685 2004-03-14 06:52:58Z baobao $
 */
public abstract class ExpressionSupport implements Expression {
    /**
     * 取得字符串表示。
     *
     * @return 表达式的字符串表示
     */
    public String toString() {
        return ClassUtil.getShortClassName(getClass()) + "[" + getExpressionText() + "]";
    }
}
