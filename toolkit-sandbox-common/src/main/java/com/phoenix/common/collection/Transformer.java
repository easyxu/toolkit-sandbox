package com.phoenix.common.collection;

/**
 * 将一个对象转换成另一个对象的接口.
 *
 * @version $Id: Transformer.java 509 2004-02-16 05:42:07Z baobao $
 * @author Michael Zhou
 */
public interface Transformer {
    /**
     * 将对象转换成另一个对象.
     *
     * @param input  被转换的对象
     *
     * @return 转换后的对象
     */
    public Object transform(Object input);
}
