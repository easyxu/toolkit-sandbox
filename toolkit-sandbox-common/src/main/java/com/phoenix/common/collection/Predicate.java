package com.phoenix.common.collection;

/**
 * "断言"接口, 基于指定的输入对象, 返回<code>true</code>或<code>false</code>. 这个接口被用于过滤器, 指定过滤的条件.
 *
 * @version $Id: Predicate.java 509 2004-02-16 05:42:07Z baobao $
 * @author Michael Zhou
 */
public interface Predicate {
    /**
     * 根据指定的输入对象, 返回<code>true</code>或<code>false</code>.
     *
     * @param input 输入对象
     *
     * @return <code>true</code>或<code>false</code>
     */
    public boolean evaluate(Object input);
}
