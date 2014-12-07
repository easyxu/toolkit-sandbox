package com.phoenix.common.expression.composite;

import com.phoenix.common.expression.ExpressionContext;
import com.phoenix.common.expression.ExpressionSupport;

/**
 * 代表一个常量表达式，该表达式的值不依赖于<code>ExpressionContext</code>。
 *
 * @author Michael Zhou
 * @version $Id: ConstantExpression.java 685 2004-03-14 06:52:58Z baobao $
 */
public class ConstantExpression extends ExpressionSupport {
    private Object value;

    /**
     * 创建一个常量表达式。
     */
    public ConstantExpression() {
    }

    /**
     * 创建一个常量表达式。
     *
     * @param value 常量值
     */
    public ConstantExpression(Object value) {
        this.value = value;
    }

    /**
     * 取得常量值。
     *
     * @return 常量值
     */
    public Object getValue() {
        return value;
    }

    /**
     * 设置常量值。
     *
     * @param value 常量值
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * 取得表达式字符串表示。
     *
     * @return 表达式字符串表示
     */
    public String getExpressionText() {
        return String.valueOf(value);
    }

    /**
     * 在指定的上下文中计算表达式。
     *
     * @param context <code>ExpressionContext</code>上下文
     *
     * @return 表达式的计算结果
     */
    public Object evaluate(ExpressionContext context) {
        return value;
    }
}