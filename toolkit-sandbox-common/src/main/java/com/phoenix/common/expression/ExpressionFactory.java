package com.phoenix.common.expression;

/**
 * 创建<code>Expression</code>的工厂。
 *
 * @author Michael Zhou
 * @version $Id: ExpressionFactory.java 685 2004-03-14 06:52:58Z baobao $
 */
public interface ExpressionFactory {
    /**
     * 创建表达式。
     *
     * @param expr 表达式字符串
     *
     * @return 表达式
     */
    Expression createExpression(String expr) throws ExpressionParseException;
}
