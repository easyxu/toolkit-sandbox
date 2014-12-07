package com.phoenix.common.expression.composite;

import java.util.List;

import com.phoenix.common.expression.Expression;
import com.phoenix.common.expression.ExpressionContext;
import com.phoenix.common.expression.ExpressionSupport;

/**
 * 代表一个组合的表达式。
 *
 * @author Michael Zhou
 * @version $Id: CompositeExpression.java 685 2004-03-14 06:52:58Z baobao $
 */
@SuppressWarnings("unchecked")
public class CompositeExpression extends ExpressionSupport {
   
	
    private String              expr;
    private Expression[]        expressions;

    /**
     * 创建一个组合的表达式。
     *
     * @param expr 表达式字符串
     * @param expressions 表达式列表
     */

	public CompositeExpression(String expr, List expressions) {
        this.expr            = expr;
        this.expressions     = (Expression[]) expressions.toArray(new Expression[expressions.size()]);
    }

    /**
     * 取得表达式字符串表示。
     *
     * @return 表达式字符串表示
     */
    public String getExpressionText() {
        return expr;
    }

    /**
     * 在指定的上下文中计算表达式。
     *
     * @param context <code>ExpressionContext</code>上下文
     *
     * @return 表达式的计算结果
     */
    public Object evaluate(ExpressionContext context) {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < expressions.length; i++) {
            Expression expression = expressions[i];
            Object     value = expression.evaluate(context);

            if (value != null) {
                buffer.append(value);
            }
        }

        return buffer.toString();
    }
}
