package com.phoenix.common.expression.jexl;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.JexlContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phoenix.common.expression.ExpressionContext;
import com.phoenix.common.expression.ExpressionSupport;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 代表一个jexl表达式。
 *
 * @author Michael Zhou
 * @version $Id: JexlExpression.java 688 2004-03-14 15:01:12Z baobao $
 */
public class JexlExpression extends ExpressionSupport {
    private static final Logger log        = LoggerFactory.getLogger(JexlExpression.class);
    private Expression          expression;

    /**
     * 创建一个Jexl表达式。
     *
     * @param expr jexl表达式对象
     */
    public JexlExpression(Expression expr) {
        this.expression = expr;
    }

    /**
     * 取得表达式字符串表示。
     *
     * @return 表达式字符串表示
     */
    public String getExpressionText() {
        return expression.getExpression();
    }

    /**
     * 在指定的上下文中计算表达式。
     *
     * @param context <code>ExpressionContext</code>上下文
     *
     * @return 表达式的计算结果
     */
    public Object evaluate(ExpressionContext context) {
        try {
            JexlContext jexlContext = new JexlContextAdapter(context);

            if (log.isDebugEnabled()) {
                log.debug("Evaluating EL: " + expression.getExpression());
            }

            Object value = expression.evaluate(jexlContext);

            if (log.isDebugEnabled()) {
                log.debug("value of expression: " + value);
            }

            return value;
        } catch (Exception e) {
            log.warn("Caught exception evaluating: " + expression + ". Reason: " + e, e);
            return null;
        }
    }

    /**
     * 将<code>ExpressionContext</code>适配到<code>JexlContext</code>。
     */
    private static class JexlContextAdapter implements JexlContext {
        @SuppressWarnings("unchecked")
		private Map vars;

        @SuppressWarnings("unchecked")
		public JexlContextAdapter(final ExpressionContext context) {
            this.vars = new Map() {
                        public Object get(Object key) {
                            return context.get((String) key);
                        }

                        public void clear() {
                        }

                        public boolean containsKey(Object key) {
                            return get(key) != null;
                        }

                        public boolean containsValue(Object value) {
                            return false;
                        }

                        @SuppressWarnings("unchecked")
						public Set entrySet() {
                            return null;
                        }

                        public boolean isEmpty() {
                            return false;
                        }

                        public Set keySet() {
                            return null;
                        }

                        public Object put(Object key, Object value) {
                            Object old = context.get((String) key);

                            context.put((String) key, value);

                            return old;
                        }

                        public void putAll(Map t) {
                        }

                        public Object remove(Object key) {
                            return null;
                        }

                        public int size() {
                            return -1;
                        }

                        public Collection values() {
                            return null;
                        }
                    };
        }

        @SuppressWarnings("unchecked")
		public void setVars(Map vars) {
        }

        @SuppressWarnings("unchecked")
		public Map getVars() {
            return this.vars;
        }
    }
}
