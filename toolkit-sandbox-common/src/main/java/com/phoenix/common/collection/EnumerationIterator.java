package com.phoenix.common.collection;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * 将<code>Enumeration</code>转换成<code>Iterator</code>的适配器.
 *
 * @version $Id: EnumerationIterator.java 509 2004-02-16 05:42:07Z baobao $
 * @author Michael Zhou
 */
@SuppressWarnings({"unchecked","unused"})
public class EnumerationIterator implements Iterator {
    private Enumeration enumeration;
	private Object      lastReturned;

    /**
     * 创建一个<code>EnumerationIterator</code>.
     *
     * @param enumeration  被适配的<code>Enumeration</code>
     */
    public EnumerationIterator(Enumeration enumeration) {
        this.enumeration = enumeration;
    }

    /**
     * 取得被适配的<code>Enumeration</code>.
     *
     * @return 被适配的<code>Enumeration</code>
     */
    public Enumeration getEnumeration() {
        return enumeration;
    }

    /**
     * 是否有下一个元素.
     *
     * @return 如果有下一个元素, 则返回<code>true</code>
     */
    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    /**
     * 取得下一个元素.
     *
     * @return 下一个元素
     */
    public Object next() {
        return (lastReturned = enumeration.nextElement());
    }

    /**
     * 删除最近返回的元素, 不支持.
     */
    public void remove() {
        throw new UnsupportedOperationException("remove() method is not supported");
    }
}
