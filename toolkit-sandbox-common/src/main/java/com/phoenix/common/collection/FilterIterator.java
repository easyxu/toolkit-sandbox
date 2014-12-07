package com.phoenix.common.collection;

import java.util.Iterator;
/**
 * Iterator的过滤器.
 *
 * @version $Id: FilterIterator.java 509 2004-02-16 05:42:07Z baobao $
 * @author Michael Zhou
 */
@SuppressWarnings("unchecked")
public abstract class FilterIterator implements Iterator {
    private Iterator iterator;

    /**
     * 创建一个过滤器.
     *
     * @param iterator  被过滤的<code>Iterator</code>
     */
    public FilterIterator(Iterator iterator) {
        this.iterator = iterator;
    }

    /**
     * 取得被过滤的<code>Iterator</code>.
     *
     * @return 被过滤的<code>Iterator</code>
     */
    public Iterator getIterator() {
        return iterator;
    }

    /**
     * 是否存在下一个元素.
     *
     * @return 如果存在下一个元素, 则返回<code>true</code>
     */
    public boolean hasNext() {
        return getIterator().hasNext();
    }

    /**
     * 取得下一个元素.
     *
     * @return 下一个元素
     */
    public Object next() {
        return getIterator().next();
    }

    /**
     * 删除最近返回的元素.
     */
    public void remove() {
        getIterator().remove();
    }
}
