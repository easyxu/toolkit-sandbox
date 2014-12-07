package com.phoenix.common.property.parser;

/**
 * 一个简单高效的缓冲池. 简单地忽略缓冲池的溢出.  当池中没有对象时, 返回<code>null</code>.
 *
 * @author <a href="mailto:zyh@alibaba-inc.com">Michael Zhou</a>
 * @version $Id: SimplePool.java 509 2004-02-16 05:42:07Z baobao $
 */
public final class SimplePool {
    /** 存放被缓冲的对象. */
    private Object[] pool;

    /** 缓冲池的大小. */
    private int capacity;

    /** 当前对象指针. */
    private int current = -1;

    /**
     * 创建缓冲池.
     *
     * @param capacity 池子容量.
     */
    public SimplePool(int capacity) {
        this.capacity     = capacity;
        this.pool         = new Object[capacity];
    }

    /**
     * 加入一个对象.  如果池子满了, 则简单地忽略.
     *
     * @param obj 需要放入池中的对象.
     */
    public synchronized void put(Object obj) {
        int i = -1;

        if (current < (capacity - 1)) {
            i = ++current;
        }

        if (i >= 0) {
            pool[i] = obj;
        }
    }

    /**
     * 从池中取得一个对象.
     *
     * @return 取得一个对象, 如果池子中没有对象, 则返回<code>null</code>.
     */
    public synchronized Object get() {
        int i = -1;

        if (current >= 0) {
            i = current--;
            return pool[i];
        }

        return null;
    }

    /**
     * 返回池子的最大容量.
     */
    public int getCapacity() {
        return capacity;
    }
}

