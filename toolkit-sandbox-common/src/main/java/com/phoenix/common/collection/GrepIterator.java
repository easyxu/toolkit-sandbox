package com.phoenix.common.collection;


import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * 根据指定的过滤条件<code>Predicate</code>, 过滤指定的<code>Iterator</code>.
 *
 * @version $Id: GrepIterator.java 509 2004-02-16 05:42:07Z baobao $
 * @author Michael Zhou
 */
@SuppressWarnings("unchecked")
public class GrepIterator extends FilterIterator {
    private Predicate predicate;
    private Object    nextObject;
    private boolean   nextObjectSet = false;

    /**
     * 创建一个<code>GrepIterator</code>.
     *
     * @param iterator  被过滤的<code>Iterator</code>
     * @param predicate 过滤条件
     */

	public GrepIterator(Iterator iterator, Predicate predicate) {
        super(iterator);
        this.predicate = predicate;
    }

    /**
     * 取得"断言"对象.
     *
     * @return "断言"对象
     */
    public Predicate getPredicate() {
        return predicate;
    }

    /**
     * 判断是否有下一个元素.
     *
     * @return 如果有下一个元素, 则返回<code>true</code>
     */
    public boolean hasNext() {
        if (nextObjectSet) {
            return true;
        } else {
            return setNextObject();
        }
    }

    /**
     * 取得下一个元素.
     *
     * @return 一下个符合条件的元素
     */
    public Object next() {
        if (!nextObjectSet && !setNextObject()) {
            throw new NoSuchElementException();
        }

        nextObjectSet = false;
        return nextObject;
    }

    /**
     * 删除最进返回的元素, 不支持.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * 设置下一个可用的元素.
     *
     * @return 如果没有下一个元素了, 则返回<code>false</code>, 否则返回<code>true</code>
     */
    private boolean setNextObject() {
        Iterator  iterator  = getIterator();
        Predicate predicate = getPredicate();

        while (iterator.hasNext()) {
            Object object = iterator.next();

            if (predicate.evaluate(object)) {
                nextObject    = object;
                nextObjectSet = true;
                return true;
            }
        }

        return false;
    }
}
