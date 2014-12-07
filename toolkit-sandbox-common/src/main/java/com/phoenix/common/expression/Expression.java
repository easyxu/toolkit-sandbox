package com.phoenix.common.expression;

/**
 * 代表一个表达式。
 *
 * @author Michael Zhou
 * @version $Id: Expression.java 685 2004-03-14 06:52:58Z baobao $
 */
public interface Expression {
    /**
     * 取得表达式字符串表示。
     *
     * @return 表达式字符串表示
     */
    String getExpressionText();

    /**
     * 在指定的上下文中计算表达式。
     *
     * @param context <code>ExpressionContext</code>上下文
     *
     * @return 表达式的计算结果
     */
    Object evaluate(ExpressionContext context);
}
