package com.phoenix.common.expression;

import java.util.HashMap;

/**
 * 使用<code>HashMap</code>实现的<code>ExpressionContext</code>。
 *
 * @author Michael Zhou
 * @version $Id: HashMapExpressionContext.java 1291 2005-03-04 03:23:30Z baobao $
 */
public class HashMapExpressionContext extends HashMap<Object,Object> implements ExpressionContext {
    

    /**
	 * 
	 */
	private static final long serialVersionUID = 4473807380877171835L;

	/**
     * 取得指定值。
     *
     * @param key 键
     *
     * @return 键对应的值
     */
    public Object get(String key) {
        return get((Object) key);
    }


    /**
     * 添加一个值。
     *
     * @param key 键
     * @param value 对应的值
     */
    public void put(String key, Object value) {
        if (value == null) {
            remove(key);
        } else {
            put((Object) key, value);
        }
    }
}
