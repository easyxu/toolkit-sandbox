package com.phoenix.common.collection;

import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * <p>
 * Hash表的一个实现, 实现了<code>Map</code>接口.
 * </p>
 *
 * <p>
 * 这个hash表的实现完全类似JDK的<code>HashMap</code>, 但作了如下改变, 以便于子类派生, 并实现特殊功能:
 * </p>
 *
 * <ul>
 * <li>
 * 将部分类成员设置成protected和friendly
 * </li>
 * <li>
 * 增加了一些方法和事件
 * </li>
 * </ul>
 *
 * <p>
 * 和JDK的<code>HashMap</code>一样, 这个实现具有以下特性:
 * </p>
 *
 * <ul>
 * <li>
 * 支持值为<code>null</code>的key和value
 * </li>
 * <li>
 * 没有进行任何<code>synchronized</code>操作, 因而不是线程安全的. 但可以通过以下操作实现线程安全:
 * </li>
 * </ul>
 *
 * <pre style="margin-left:48.0">
 *  Map m = Collections.synchronizedMap(new DefaultHashMap(...));
 * </pre>
 *
 * <ul>
 * <li>
 * 不保证hash表中的entry的顺序
 * </li>
 * <li>
 * 以几乎衡定的性能存取hash表中的每个entry
 * </li>
 * <li>
 * 从hash表中取得的任何<code>Iterator</code>具有<i>fail-fast</i>特性: 当hash表的结构被改变时,
 * 调用<code>Iterator.remove</code>和<code>Iterator.add</code>方法时,
 * 会掷出<code>ConcurrentModificationException</code>. 这样确保不会出现不确定的情况.
 * </li>
 * </ul>
 *
 *
 * @version $Id: DefaultHashMap.java 509 2004-02-16 05:42:07Z baobao $
 * @author Michael Zhou
 */
@SuppressWarnings("unchecked")
public class DefaultHashMap extends AbstractMap implements Map, Cloneable, Serializable {
    /* ============================================================================ */
    /* 常量                                                                         */
    /* ============================================================================ */

    /** 默认的初始容量 - <code>2的整数次幂</code>. */
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    /** 最大容量 - <code>2的整数次幂</code>. */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /** 默认的负载系数 */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /* ============================================================================ */
    /* 成员变量                                                                     */
    /* ============================================================================ */

    /** Hash表, 长度可变 - 但长度必须是<code>2的整数次幂</code>. */
    protected transient Entry[] table;

    /** Hash表中的entry数. */
    protected transient int size;

    /**
     * 阈值. 当hash表中的entry数超过它时, 自动扩容(<code>resize</code>).
     * 其值等于<code>capacity&times;loadFactor</code>.
     *
     * @serial 自动序列化字段
     */
    protected int threshold;

    /**
     * 负载系数.
     *
     * @serial 自动序列化字段
     */
    protected final float loadFactor;

    /**
     * 当hash表发生&quot;结构改变&quot;的计数. 所谓&quot;结构改变&quot;,
     * 是指hash表中entry的数目发生改变或内部结构改变(如<code>resize</code>). 这个字段是为了实现<i>fail-fast</i>.
     */
    protected transient volatile int modCount;

    /** key的集合视图. */
    private transient Set keySet = null;

    /** entry的集合视图. */
    private transient Set entrySet = null;

    /** value的集合视图. */
    private transient Collection values = null;

    /* ============================================================================ */
    /* 构造函数                                                                     */
    /* ============================================================================ */

    /**
     * 创建一个空的hash表. 使用指定的默认的初始容量(16)和默认的负载系数(0.75).
     */
    public DefaultHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * 创建一个空的hash表. 使用指定的初始阈值和默认的负载系数(0.75).
     *
     * @param initialCapacity 初始容量.
     */
    public DefaultHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * 创建一个空的hash表. 使用指定的初始容量和负载系数.
     *
     * @param initialCapacity 初始容量
     * @param loadFactor 负载系数.
     */
    public DefaultHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }

        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        if ((loadFactor <= 0) || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        // 确保初始容量为2的整数次幂.
        int capacity = 1;

        while (capacity < initialCapacity) {
            capacity <<= 1;
        }

        this.loadFactor = loadFactor;
        this.threshold  = (int) (capacity * loadFactor);
        this.table      = new Entry[capacity];

        onInit();
    }

    /**
     * 复制指定<code>Map</code>内容相同的<code>HashMap</code>. 使用默认的负载系数(0.75).
     *
     * @param map 要复制的<code>Map</code>
     */
    public DefaultHashMap(Map map) {
        this(Math.max((int) (map.size() / DEFAULT_LOAD_FACTOR) + 1, DEFAULT_INITIAL_CAPACITY),
             DEFAULT_LOAD_FACTOR);
        putAllForCreate(map);
    }

    /* ============================================================================ */
    /* 实现Map接口的方法                                                            */
    /* ============================================================================ */

    /**
     * 返回hash表中entry的个数.
     *
     * @return hash表中的entry数.
     */
    public int size() {
        return size;
    }

    /**
     * 判断是否为空的hash表.
     *
     * @return 如果为空(<code>size() == 0</code>), 则返回<code>true</code>.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 返回指定key对应的value. 如果hash表中没有value对应key, 则返回<code>null</code>.
     * 但是返回<code>null</code>并不总是代表没有value对应指定的key, 也有可能是指 value值本身为<code>null</code>.
     * 可以通过方法<code>containsKey</code>来区分这两 种情况.
     *
     * @param key 指定key所对应的value将被返回.
     *
     * @return 指定key对应的value, 如果没有value对应此key, 则返回<code>null</code>.
     */
    public Object get(Object key) {
        Entry entry = getEntry(key);

        return (entry == null) ? null
                               : entry.getValue();
    }

    /**
     * 如果hash表中包含指定key的entry, 则返回<code>true</code>.
     *
     * @param key   测试指定的key是否存在.
     *
     * @return 如果key对应的entry存在, 则返回<code>true</code>.
     */
    public boolean containsKey(Object key) {
        Entry entry = getEntry(key);

        return entry != null;
    }

    /**
     * 将指定的value和key关联. 如果已经有value和此key相关联, 则取代之, 并 返回被取代的value.
     *
     * @param key 要关联的key
     * @param value 要和key关联的value
     *
     * @return 如果已经存在和此key相关联的value, 则返回此value. 否则返回<code>null</code>.
     *         返回<code>null</code>也可能是因为被取代的这个value值为<code>null</code>.
     */
    public Object put(Object key, Object value) {
        Entry entry = getEntry(key);

        if (entry != null) {
            Object oldValue = entry.getValue();

            entry.setValue(value);
            entry.onAccess();

            return oldValue;
        } else {
            modCount++;

            // 如果表中的项数即将超过阈值, 则容量倍增.
            if (size >= threshold) {
                resize(table.length * 2);
            }

            addEntry(key, value);

            return null;
        }
    }

    /**
     * 将<code>Map</code>中的所有项都加入到当前的<code>Map</code>中. 如果有相同的key, 则替换之.
     *
     * @param map 要加入的<code>Map</code>
     */
    public void putAll(Map map) {
        // 一次性扩容, 以便容纳性加入的entry.
        int n = map.size();

        if (n == 0) {
            return;
        }

        if (n >= threshold) {
            n = (int) (n / loadFactor + 1);

            if (n > MAXIMUM_CAPACITY) {
                n = MAXIMUM_CAPACITY;
            }

            int capacity = table.length;

            while (capacity < n) {
                capacity <<= 1;
            }

            resize(capacity);
        }

        for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();

            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 将指定key的entry从hash表中删除(如果该entry存在的话).
     *
     * @param key 要被删除的entry的key
     *
     * @return 被删除的entry的value. 如果entry不存在, 则返回<code>null</code>.
     *         但是返回<code>null</code>并不总是代表没有value对应指定的key, 也有可能是指 value值本身为<code>null</code>.
     */
    public Object remove(Object key) {
        Entry entry = removeEntryForKey(key);

        return ((entry == null) ? null
                                : entry.getValue());
    }

    /**
     * 清除hash表中的所有entry.
     */
    public void clear() {
        modCount++;
        Arrays.fill(table, null);
        size = 0;
    }

    /**
     * 判断hash表中是否有一个或多个entry具有指定的value.
     *
     * @param value 要测试的value
     *
     * @return 如果有一个或多个entry具有指定的value, 则返回<code>true</code>
     */
    public boolean containsValue(Object value) {
        Entry[] tab = table;

        for (int i = 0; i < tab.length; i++) {
            for (Entry entry = tab[i]; entry != null; entry = entry.next) {
                if (eq(value, entry.getValue())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 取得key的集合视图. 这个集合是以hash表为基础的, 任何hash表的改变, 都会反射到这个集合, 反之亦然. 该集合支持删除操作,
     * 删除集合中的key就删除了hash表中的相应entry. 可以通过以下方法删除一个entry: <code>Iterator.remove</code>,
     * <code>Set.remove</code>, <code>removeAll</code>, <code>retainAll</code>,
     * 和<code>clear</code>. 但集合不支持<code>add</code>或<code>addAll</code>操作.
     *
     * @return key的集合视图
     */
    public Set keySet() {
        Set ks = keySet;

        return ((ks != null) ? ks
                             : (keySet = new KeySet()));
    }

    /**
     * 取得value的集合视图. 这个集合是以hash表为基础的, 任何hash表的改变, 都会反射到这个集合, 反之亦然. 该集合支持删除操作,
     * 删除集合中的key就删除了hash表中的相应entry. 可以通过以下方法删除一个entry: <code>Iterator.remove</code>,
     * <code>Collection.remove</code>, <code>removeAll</code>, <code>retainAll</code>,
     * 和<code>clear</code>. 但集合不支持<code>add</code>或<code>addAll</code>操作.
     *
     * @return value的集合视图
     */
    public Collection values() {
        Collection vs = values;

        return ((vs != null) ? vs
                             : (values = new Values()));
    }

    /**
     * 取得entry的集合视图. 这个集合是以hash表为基础的, 任何hash表的改变, 都会反射到这个集合, 反之亦然. 该集合支持删除操作,
     * 删除集合中的key就删除了hash表中的相应entry. 可以通过以下方法删除一个entry: <code>Iterator.remove</code>,
     * <code>Set.remove</code>, <code>removeAll</code>, <code>retainAll</code>,
     * 和<code>clear</code>. 但集合不支持<code>add</code>或<code>addAll</code>操作.
     *
     * @return entry的集合视图
     */
    public Set entrySet() {
        Set es = entrySet;

        return ((es != null) ? es
                             : (entrySet = new EntrySet()));
    }

    /* ============================================================================ */
    /* 内部类                                                                       */
    /* ============================================================================ */

    /**
     * <code>Map.Entry</code>的实现.
     */
    protected static class Entry extends DefaultMapEntry {
        /** key的hash值. */
        protected final int hash;

        /** 相同hash值的entry是以链表的方式存放的, 这个引用指向链表中的下一个entry. */
        protected Entry next;

        /**
         * 创建一个新的entry.
         *
         * @param h key的hash值
         * @param k entry的key
         * @param v entry的value
         * @param n 链表中的下一个entry
         */
        protected Entry(int h, Object k, Object v, Entry n) {
            super(k, v);
            next = n;
            hash = h;
        }

        /**
         * 当<code>put(key, value)</code>方法被调用时, 如果entry已经存在将被覆盖时, 此方法被调用.
         */
        protected void onAccess() {
        }

        /**
         * 当entry将被删除时, 此方法被调用.
         */
        protected void onRemove() {
        }
    }

    /**
     * 遍历器.
     */
    private abstract class HashIterator implements Iterator {
        /** 当前entry. */
        private Entry current;

        /** 下一个要返回的entry. */
        private Entry next;

        /** 创建iterator时的修改计数. */
        private int expectedModCount;

        /** 当前位置索引. */
        private int index;

        /**
         * 创建一个遍历器.
         */
        protected HashIterator() {
            expectedModCount = modCount;

            Entry[] t = table;
            int     i = t.length;
            Entry   n = null;

            if (size != 0) { // advance to first entry

                while ((i > 0) && ((n = t[--i]) == null)) {
                    ;
                }
            }

            next  = n;
            index = i;
        }

        /**
         * 返回遍历器中是否还有下一个entry.
         *
         * @return 如果遍历器中还有下一个entry, 返回<code>true</code>
         */
        public boolean hasNext() {
            return next != null;
        }

        /**
         * 删除一个当前entry. 执行前必须先执行<code>next()</code>方法.
         */
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }

            checkForComodification();

            Object k = current.getKey();

            current = null;
            DefaultHashMap.this.removeEntryForKey(k);
            expectedModCount = modCount;
        }

        /**
         * 取得下一个entry.
         *
         * @return 下一个entry
         */
        protected Entry nextEntry() {
            checkForComodification();

            Entry entry = next;

            if (entry == null) {
                throw new NoSuchElementException();
            }

            Entry   n = entry.next;
            Entry[] t = table;
            int     i = index;

            while ((n == null) && (i > 0)) {
                n = t[--i];
            }

            index = i;
            next  = n;

            return current = entry;
        }

        /**
         * 检查是否同时被修改.
         */
        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * 取得hash表的key的遍历器.
     */
    private class KeyIterator extends HashIterator {
        /**
         * 取得下一个key.
         *
         * @return 下一个key
         */
        public Object next() {
            return nextEntry().getKey();
        }
    }

    /**
     * 取得hash表的value的遍历器.
     */
    private class ValueIterator extends HashIterator {
        /**
         * 取得下一个value.
         *
         * @return 下一个value
         */
        public Object next() {
            return nextEntry().getValue();
        }
    }

    /**
     * 取得hash表的entry的遍历器.
     */
    private class EntryIterator extends HashIterator {
        /**
         * 取得下一个entry.
         *
         * @return 下一个entry
         */
        public Object next() {
            return nextEntry();
        }
    }

    /**
     * key的集合视图.
     */
    private class KeySet extends AbstractSet {
        /**
         * 取得key的遍历器.
         *
         * @return key的遍历器
         */
        public Iterator iterator() {
            return newKeyIterator();
        }

        /**
         * 取得集合的大小, 就是hash表中entry的数量.
         *
         * @return hash表中entry的数量
         */
        public int size() {
            return size;
        }

        /**
         * 判断key中是否包含指定对象.
         *
         * @param o 要查找的对象
         *
         * @return 如果key中包含指定的对象, 则返回<code>true</code>
         */
        public boolean contains(Object o) {
            return containsKey(o);
        }

        /**
         * 从hash表中删除key为指定对象的entry.
         *
         * @param o 指定的key
         *
         * @return 如果删除成功, 则返回<code>true</code>
         */
        public boolean remove(Object o) {
            return DefaultHashMap.this.removeEntryForKey(o) != null;
        }

        /**
         * 清除所有entry.
         */
        public void clear() {
            DefaultHashMap.this.clear();
        }
    }

    /**
     * value的集合视图.
     */
    private class Values extends AbstractCollection {
        /**
         * 取得value的遍历器.
         *
         * @return value的遍历器
         */
        public Iterator iterator() {
            return newValueIterator();
        }

        /**
         * 取得集合的大小, 就是hash表中entry的数量.
         *
         * @return hash表中entry的数量
         */
        public int size() {
            return size;
        }

        /**
         * 判断value中是否包含指定对象.
         *
         * @param o 要查找的对象
         *
         * @return 如果value中包含指定的对象, 则返回<code>true</code>
         */
        public boolean contains(Object o) {
            return containsValue(o);
        }

        /**
         * 清除所有entry.
         */
        public void clear() {
            DefaultHashMap.this.clear();
        }
    }

    /**
     * entry的集合视图.
     */
    private class EntrySet extends AbstractSet {
        /**
         * 取得entry的遍历器.
         *
         * @return entry的遍历器
         */
        public Iterator iterator() {
            return newEntryIterator();
        }

        /**
         * 判断entry集合中是否包含指定对象.
         *
         * @param o 要查找的对象
         *
         * @return 如果entry中是否包含指定对象, 则返回<code>true</code>
         */
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }

            Map.Entry entry     = (Map.Entry) o;
            Entry     candidate = getEntry(entry.getKey());

            return eq(candidate, entry);
        }

        /**
         * 从hash表中删除指定entry.
         *
         * @param o 要删除的entry
         *
         * @return 如果删除成功, 则返回<code>true</code>
         */
        public boolean remove(Object o) {
            return removeEntry(o) != null;
        }

        /**
         * 取得集合的大小, 就是hash表中entry的数量.
         *
         * @return hash表中entry的数量
         */
        public int size() {
            return size;
        }

        /**
         * 清除所有entry.
         */
        public void clear() {
            DefaultHashMap.this.clear();
        }
    }

    /* ============================================================================ */
    /* 序列化                                                                       */
    /* ============================================================================ */

    /** 序列化版本号. */
    private static final long serialVersionUID = 362498820763181265L;

    /**
     * 从输入流中重建hash表(也就是反序列化).
     *
     * @param is 输入流
     *
     * @exception java.io.IOException 输入流异常
     * @exception ClassNotFoundException 类未找到
     */
    private void readObject(java.io.ObjectInputStream is) throws IOException,
                                                                 ClassNotFoundException {
        // 读入threshold, loadfactor, 和其它隐藏的对象.
        is.defaultReadObject();

        // 取得hash表的容量.
        int numBuckets = is.readInt();

        table = new Entry[numBuckets];


        // 给子类一个机会初始化.
        onInit();

        // 读入hash表中entry的个数.
        int size = is.readInt();

        // 读入所有的entry.
        for (int i = 0; i < size; i++) {
            Object key   = is.readObject();
            Object value = is.readObject();

            putForCreate(key, value);
        }
    }

    /**
     * 将hash表的状态保存到输出流中(也就是&quot;序列化&quot;).
     *
     * @param os 输出流
     *
     * @exception java.io.IOException 输出流异常
     */
    private void writeObject(java.io.ObjectOutputStream os) throws IOException {
        // 输出threshold, loadfactor, 和其它隐藏的对象.
        os.defaultWriteObject();


        // 输出hash表的容量.
        os.writeInt(table.length);


        // 输出hash表的大小.
        os.writeInt(size);

        // 输出所有entry.
        for (Iterator i = entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();

            os.writeObject(entry.getKey());
            os.writeObject(entry.getValue());
        }
    }

    /* ============================================================================ */
    /* 复制方法(Clonable接口)                                                       */
    /* ============================================================================ */

    /**
     * &quot;浅&quot;拷贝hash表, key和value本身并不被复制.
     *
     * @return 被复制的hash表.
     */
    public Object clone() {
        DefaultHashMap result = null;

        try {
            result = (DefaultHashMap) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(); // 不支持clone(不可能).
        }

        result.table    = new Entry[table.length];
        result.entrySet = null;
        result.modCount = 0;
        result.size     = 0;
        result.onInit();
        result.putAllForCreate(this);

        return result;
    }

    /* ============================================================================ */
    /* 内部方法                                                                     */
    /* ============================================================================ */

    /**
     * 给子类一个机会初始化自己. 该方法被所有构造函数以及&quot;伪构造函数&quot;(<code>clone</code>, <code>readObject</code>)调用.
     * 调用时, hash表已被初始化, 但数据尚未被插入到表中.
     */
    protected void onInit() {
    }

    /**
     * 返回指定key对应的entry. 如果不存在, 则返回null.
     *
     * @param key 返回指定key对应的entry
     *
     * @return 指定key对应的entry
     */
    protected Entry getEntry(Object key) {
        int hash = hash(key);
        int i = indexFor(hash, table.length);

        for (Entry entry = table[i]; entry != null; entry = entry.next) {
            if ((entry.hash == hash) && eq(key, entry.getKey())) {
                return entry;
            }
        }

        return null;
    }

    /**
     * 加入一个entry到hash表中, 但不会对hash表进行<code>resize()</code>操作. 子类可以覆盖此方法, 以改变<code>put</code>,
     * <code>new HashMap(Map)</code>, <code>clone</code>, 和<code>readObject</code>方法的行为.
     *
     * @param key hash表的key
     * @param value hash表的value
     */
    protected void addEntry(Object key, Object value) {
        int hash = hash(key);
        int i = indexFor(hash, table.length);

        table[i] = new Entry(hash, key, value, table[i]);
        size++;
    }

    /**
     * 此方法被构造函数或&quot;伪构造函数&quot;(clone, readObject)调用, 功能同put方法, 但不会调用resize或改变modCount计数.
     *
     * @param key 要关联的key
     * @param value 要和key关联的value
     */
    private void putForCreate(Object key, Object value) {
        Entry entry = getEntry(key);

        if (entry != null) {
            entry.setValue(value);
        } else {
            addEntry(key, value);
        }
    }

    /**
     * 一次put多个entry.
     *
     * @param map 指定map的所有entry都被放入hash表中
     */
    private void putAllForCreate(Map map) {
        for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();

            putForCreate(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 删除指定key对应的entry, 并返回被删除的entry.
     *
     * @param key 要删除的entry的key
     *
     * @return 被删除的entry, 如果entry不存在, 则返回<code>null</code>
     */
    protected Entry removeEntryForKey(Object key) {
        int   hash  = hash(key);
        int   i     = indexFor(hash, table.length);
        Entry prev  = table[i];
        Entry entry = prev;

        while (entry != null) {
            Entry next = entry.next;

            if ((entry.hash == hash) && eq(key, entry.getKey())) {
                modCount++;
                size--;

                if (prev == entry) {
                    table[i] = next;
                } else {
                    prev.next = next;
                }

                entry.onRemove();

                return entry;
            }

            prev  = entry;
            entry = next;
        }

        return entry;
    }

    /**
     * 删除指定的entry. 这个方法用于<code>EntrySet.remove</code>.
     *
     * @param o 要删除的entry
     *
     * @return 被删除的entry
     */
    protected Entry removeEntry(Object o) {
        if (!(o instanceof Map.Entry)) {
            return null;
        }

        Map.Entry entry = (Map.Entry) o;
        Object    key  = entry.getKey();
        int       hash = hash(key);
        int       i    = indexFor(hash, table.length);
        Entry     prev = table[i];
        Entry     e    = prev;

        while (e != null) {
            Entry next = e.next;

            if ((e.hash == hash) && e.equals(entry)) {
                modCount++;
                size--;

                if (prev == e) {
                    table[i] = next;
                } else {
                    prev.next = next;
                }

                e.onRemove();

                return e;
            }

            prev = e;
            e    = next;
        }

        return e;
    }

    /**
     * 子类覆盖此方法, 用来创建key的遍历器.
     *
     * @return hash表的key的遍历器
     */
    protected Iterator newKeyIterator() {
        return new KeyIterator();
    }

    /**
     * 子类覆盖此方法, 用来创建value的遍历器.
     *
     * @return hash表的key的遍历器
     */
    protected Iterator newValueIterator() {
        return new ValueIterator();
    }

    /**
     * 子类覆盖此方法, 用来创建entry的遍历器.
     *
     * @return hash表的key的遍历器
     */
    protected Iterator newEntryIterator() {
        return new EntryIterator();
    }

    /**
     * 返回对象的hash值.
     *
     * @param obj 取得指定对象的hash值
     *
     * @return 指定对象的hash值
     */
    protected static int hash(Object obj) {
        int h = (obj == null) ? 0
                              : obj.hashCode();

        return h - (h << 7); // 也就是, -127 * h
    }

    /**
     * 比较两个对象.
     *
     * @param x 第一个对象
     * @param y 第二个对象
     *
     * @return 如果相同, 则返回<code>true</code>
     */
    protected static boolean eq(Object x, Object y) {
        return (x == null) ? (y == null)
                           : ((x == y) || x.equals(y));
    }

    /**
     * 返回索引值, 根据指定的hash值和数组的长度.
     *
     * @param hash hash值
     * @param length 数组的长度, 必然是2的整数次幂
     *
     * @return hash值在数组中的序号
     */
    protected static int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    /**
     * 对map进行扩容.  此方法在entry数超过阈值时被调用.
     *
     * @param newCapacity 新的容量(必须为2的整数次幂).
     */
    protected void resize(int newCapacity) {
        Entry[] oldTable    = table;
        int     oldCapacity = oldTable.length;

        if ((size < threshold) || (oldCapacity > newCapacity)) {
            return;
        }

        Entry[] newTable = new Entry[newCapacity];

        transfer(newTable);
        table     = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    /**
     * 将所有entry从当前表中移到新表中(扩容).
     *
     * @param newTable 新表
     */
    protected void transfer(Entry[] newTable) {
        Entry[] src         = table;
        int     newCapacity = newTable.length;

        for (int j = 0; j < src.length; j++) {
            Entry entry = src[j];

            if (entry != null) {
                src[j] = null;

                do {
                    Entry next = entry.next;
                    int   i = indexFor(entry.hash, newCapacity);

                    entry.next  = newTable[i];
                    newTable[i] = entry;
                    entry       = next;
                } while (entry != null);
            }
        }
    }

    /**
     * 取得hash表的容量.
     *
     * @return hash表的容量
     */
    protected int getCapacity() {
        return table.length;
    }

    /**
     * 取得hash表的负载系数.
     *
     * @return hash表的负载系数
     */
    protected float getLoadFactor() {
        return loadFactor;
    }

    /**
     * 取得hash表的阈值.
     *
     * @return hash表的阈值
     */
    protected int getThreshold() {
        return threshold;
    }
}