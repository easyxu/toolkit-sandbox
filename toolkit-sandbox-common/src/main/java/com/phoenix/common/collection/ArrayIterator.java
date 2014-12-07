package com.phoenix.common.collection;

import java.lang.reflect.Array;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 对数组实现<code>Iterator</code>接口.
 *
 * @version $Id: ArrayIterator.java 509 2004-02-16 05:42:07Z baobao $
 * @author Michael Zhou
 */
@SuppressWarnings("unchecked")
public class ArrayIterator implements Iterator {
    private Object array;
    private int    length = 0;
    private int    index  = 0;

    /**
     * 创建一个<code>ArrayIterator</code>.
     *
     * @param array  要遍历的数组
     */
    public ArrayIterator(Object array) {
        this.array  = array;
        this.length = Array.getLength(array);
    }

    /**
     * 取得被遍历的数组.
     *
     * @return 被遍历的数组
     */
    public Object getArray() {
        return array;
    }

    /**
     * 是否还有下一个数组元素.
     *
     * @return 如果还有下一个数组元素, 则返回<code>true</code>
     */
    public boolean hasNext() {
        return index < length;
    }

    /**
     * 取得下一个数组元素.
     *
     * @return 下一个数组元素
     */
    public Object next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return Array.get(array, index++);
    }

    /**
     * 删除当前元素, 不支持.
     */
    public void remove() {
        throw new UnsupportedOperationException("remove() method is not supported");
    }
}
