package com.phoenix.common.property;

import java.io.ObjectStreamException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.phoenix.common.collection.ArrayHashMap;
import com.phoenix.common.collection.ListMap;
import com.phoenix.common.convert.Convert;

/**
 * 一个简单的DOM结构.
 *
 * @author Michael Zhou
 * @version $Id: Property.java 1291 2005-03-04 03:23:30Z baobao $
 */
@SuppressWarnings("unchecked")
public class Property implements Cloneable, Serializable {
    

    /**
	 * 
	 */
	private static final long serialVersionUID = 4732297050649101957L;
	  @SuppressWarnings("unchecked")
	public static final Property NULL = new Property(null) {
            

            /**
		 * 
		 */
		private static final long serialVersionUID = -4631914840391096025L;

			public Object getKey() {
                return null;
            }

            public Object getValue() {
                return null;
            }

            public Object setValue(Object newValue) {
                return null;
            }

            public boolean exists() {
                return false;
            }

            public int childCount() {
                return 0;
            }

            public int size() {
                return 0;
            }

            public boolean isList() {
                return false;
            }
            @SuppressWarnings("unused" )
            public boolean isMap() {
                return false;
            }

            public Property child(Object key) {
                return this;
            }

            public Property child(int index) {
                return this;
            }

            public Property index(int index) {
                return this;
            }

            public Property ensureChild(Object key) {
                return null;
            }

            public Property ensureIndex(int index) {
                return null;
            }

            public String toString() {
                return "NULL";
            }

            public Object readResolve() throws ObjectStreamException {
                return NULL;
            }

            public Object clone() {
                return NULL;
            }
        };

    private Object key;
    private Object value;
    private Object children;

    public Property(Object key) {
        this.key = key;
    }

    public Property(Object key, Object value) {
        this.key       = key;
        this.value     = value;
    }

    private Property(Object key, Object value, Object children) {
        this.key          = key;
        this.value        = value;
        this.children     = children;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return intern().value;
    }

    public Object setValue(Object newValue) {
        Property intern   = intern();
        Object   oldValue = intern.value;

        intern.value = newValue;
        return oldValue;
    }

    public Property intern() {
        Property next = this;

        while (next.children instanceof List) {
            next = (Property) ((List) next.children).get(0);
        }

        return next;
    }

    public boolean exists() {
        return true;
    }

    public int childCount() {
        Property intern = intern();

        if (intern.children instanceof ListMap) {
            return ((ListMap) intern.children).size();
        }

        return 0;
    }

    public int size() {
        if (children instanceof List) {
            return ((List) children).size();
        }

        return 1;
    }

    public boolean isList() {
        return children instanceof List;
    }

    public boolean hasChild() {
        return children instanceof ListMap;
    }

    public Property child(Object key) {
        Property child  = null;
        Property intern = intern();

        if (intern.children instanceof ListMap) {
            child = (Property) ((ListMap) intern.children).get(key);
        }

        return (child == null) ? NULL
                               : child;
    }

    public Property child(int index) {
        Property child  = null;
        Property intern = intern();

        if (intern.children instanceof ListMap && (index >= 0)
                    && (index < ((ListMap) intern.children).size())) {
            child = (Property) ((ListMap) intern.children).get(index);
        }

        return (child == null) ? NULL
                               : child;
    }

    public Property index(int index) {
        Property child = null;

        if (children instanceof List) {
            if ((index >= 0) && (index < ((List) children).size())) {
                child = (Property) ((List) children).get(index);
            }
        } else if (index == 0) {
            child = this;
        }

        return (child == null) ? NULL
                               : child;
    }

    public Property ensureChild(Object key) {
        Property intern = intern();

        if (intern.children == null) {
            intern.children = createMap();
        }

        ListMap childMap = (ListMap) intern.children;

        if (childMap.containsKey(key)) {
            return (Property) childMap.get(key);
        } else {
            Property child = new Property(key);

            childMap.put(key, child);
            return child;
        }
    }

  
	public Property ensureIndex(int index) {
        if (index < 0) {
            return null;
        }

        List childList;

        if (children instanceof List) {
            childList = (List) children;
        } else {
            Property child = new Property(key, value, children);

            children     = childList = createList();
            value        = null;
            childList.add(child);
        }

        for (int i = childList.size(); i <= index; i++) {
            childList.add(new Property(key));
        }

        return (Property) childList.get(index);
    }

    private ListMap createMap() {
        return new ArrayHashMap();
    }

    private List createList() {
        return new ArrayList();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Property)) {
            return false;
        }

        Property p = (Property) o;

        if (children == null) {
            if (p.children == null) {
                return ((key == null) ? (p.key == null)
                                      : key.equals(p.key))
                && ((value == null) ? (p.value == null)
                                    : value.equals(p.value));
            }

            return false;
        }

        if (children instanceof List) {
            if (p.children instanceof List) {
                return children.equals(p.children);
            }

            return false;
        }

        if (children instanceof ListMap) {
            if (p.children instanceof ListMap) {
                return ((key == null) ? (p.key == null)
                                      : key.equals(p.key))
                && ((value == null) ? (p.value == null)
                                    : value.equals(p.value)) && children.equals(p.children);
            }

            return false;
        }

        return false;
    }

    /**
     * @todo 让list.size为1等同于非list
     */
    public int hashCode() {
        int hash = ((key == null) ? 0
                                  : key.hashCode()) ^ ((children == null) ? 0
                                                                          : children.hashCode());

        if (!(children instanceof List)) {
            hash ^= ((value == null) ? 0
                                     : value.hashCode());
        }

        return hash;
    }

    public String toString() {
        return toString(true);
    }

    private String toString(boolean showKey) {
        StringBuffer buffer = new StringBuffer();

        if (children == null) {
            if (showKey) {
                buffer.append(key).append('=');
            }

            buffer.append(value);
        } else if (children instanceof List) {
            List childList = (List) children;

            if (showKey) {
                buffer.append(key).append('=');
            }

            buffer.append("LIST[");

            for (Iterator i = childList.iterator(); i.hasNext();) {
                buffer.append(((Property) i.next()).toString(false));

                if (i.hasNext()) {
                    buffer.append(',');
                }
            }

            buffer.append(']');
        } else {
            ListMap childMap = (ListMap) children;

            if (showKey) {
                buffer.append(key).append('=');
            }

            if (value != null) {
                buffer.append(value).append(';');
            }

            buffer.append("CHILD{");

            for (Iterator i = childMap.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();

                buffer.append(entry.getKey()).append('=');
                buffer.append(((Property) entry.getValue()).toString(false));

                if (i.hasNext()) {
                    buffer.append(',');
                }
            }

            buffer.append('}');
        }

        return buffer.toString();
    }

    public Object clone() {
        Property copy = new Property(key);

        if (children == null) {
            copy.value = value;
        } else if (children instanceof List) {
            copy.children = createList();

            List childList = (List) copy.children;

            for (Iterator i = ((List) children).iterator(); i.hasNext();) {
                childList.add(((Property) i.next()).clone());
            }
        } else {
            copy.value        = value;
            copy.children     = createMap();

            ListMap childMap  = (ListMap) copy.children;

            for (Iterator i = ((ListMap) children).entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();

                childMap.put(entry.getKey(), ((Property) entry.getValue()).clone());
            }
        }

        return copy;
    }

    /**
     * @todo 测试当childList.size() == 1并且intern.children != null时的bug
     */
    public Property removeIndex(int index) {
        Property old = null;

        if (children instanceof List) {
            List childList = (List) children;

            if ((index >= 0) && (index < childList.size())) {
                old = (Property) childList.remove(index);

                if (childList.size() == 1) {
                    Property intern = intern();

                    value        = intern.value;
                    children     = intern.children;
                }
            }
        }

        return old;
    }

    public Property removeChild(int index) {
        Property old    = null;
        Property intern = intern();

        if (intern.children instanceof ListMap) {
            ListMap childMap = (ListMap) intern.children;

            if ((index >= 0) && (index < childMap.size())) {
                old = (Property) ((Map.Entry) childMap.remove(index)).getValue();

                if (childMap.isEmpty()) {
                    intern.children = null;
                }
            }
        }

        return old;
    }

    public Property removeChild(Object key) {
        Property old    = null;
        Property intern = intern();

        if (intern.children instanceof ListMap) {
            ListMap childMap = (ListMap) intern.children;

            old = (Property) childMap.remove(key);

            if (childMap.isEmpty()) {
                intern.children = null;
            }
        }

        return old;
    }

    public boolean tidy() {
        if (children == null) {
            return value == null;
        } else if (children instanceof List) {
            boolean remove    = true;
            List    childList = (List) children;

            for (int i = childList.size() - 1; i >= 0; i--) {
                Property child = (Property) childList.get(i);

                if (!child.tidy()) {
                    if (childList.size() == 1) {
                        Property intern = intern();

                        value        = intern.value;
                        children     = intern.children;
                    }

                    remove = false;
                }

                if (remove) {
                    childList.remove(i);
                }
            }

            if (remove) {
                children = null;
            }

            return remove;
        } else {
            boolean allNull  = true;
            ListMap childMap = (ListMap) children;

            for (Iterator i = childMap.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();
                Property  child = (Property) entry.getValue();

                if (child.tidy()) {
                    i.remove();
                } else {
                    allNull = false;
                }
            }

            if (allNull) {
                children = null;
            }

            return allNull && (value == null);
        }
    }

    public void display() {
        display(new OutputStreamWriter(System.out));
    }

    public void display(Writer writer) {
        PrintWriter out = (writer instanceof PrintWriter) ? (PrintWriter) writer
                                                          : new PrintWriter(writer);

        display(this, out, "", true, true);
        out.flush();
    }

    private static final int    KEY_MAX_LENGTH     = 15;
    private static final int    VALUE_MAX_LENGTH   = 30;
    private static final String EMPTY_LINE         = " |";
    private static final String VERTICAL_LINE      = " |    ";
    private static final String LAST_VERTICAL_LINE = "      ";
    private static final String BRANCH             = " +--- ";
    private static final String LAST_BRANCH        = " \\--- ";

    private boolean display(Property p, PrintWriter out, String prefix, boolean showKey,
        boolean showColon) {
        boolean displayNewLine = false;

        if (showKey) {
            out.print(prefix);
            out.print(toString(p.key, KEY_MAX_LENGTH));
        }

        if (p.children == null) {
            out.print(" = ");
            out.print(toString(p.value, VALUE_MAX_LENGTH));
        } else if (p.children instanceof List) {
            List childList = (List) p.children;

            if (showColon) {
                out.print(':');
            }

            out.print(' ');
            out.print("LIST[");
            out.print(childList.size());
            out.println(']');
            out.print(prefix);
            out.print(EMPTY_LINE);

            int index = 0;

            for (Iterator i = childList.iterator(); i.hasNext(); index++) {
                out.println();
                out.print(prefix);

                Property q = (Property) i.next();

                if (i.hasNext()) {
                    out.print(BRANCH);
                } else {
                    out.print(LAST_BRANCH);
                    displayNewLine = true;
                }

                out.print('[');
                out.print(index);
                out.print(']');

                if (display(q, out, prefix + (i.hasNext() ? VERTICAL_LINE
                                                                  : LAST_VERTICAL_LINE), false,
                                false) && !displayNewLine) {
                    out.println();
                    out.print(prefix);
                    out.print(EMPTY_LINE);
                }
            }
        } else {
            ListMap childMap = (ListMap) p.children;

            if (showColon) {
                out.print(':');
            }

            out.print(' ');
            out.print("CHILD[");
            out.print(childMap.size());
            out.print(']');

            if (p.value != null) {
                out.print(" = ");
                out.print(toString(p.value, VALUE_MAX_LENGTH));
            }

            out.println();
            out.print(prefix);
            out.print(EMPTY_LINE);

            for (Iterator i = childMap.entrySet().iterator(); i.hasNext();) {
                out.println();
                out.print(prefix);

                Map.Entry entry = (Map.Entry) i.next();

                if (i.hasNext()) {
                    out.print(BRANCH);
                } else {
                    out.print(LAST_BRANCH);
                    displayNewLine = true;
                }

                out.print(toString(entry.getKey(), KEY_MAX_LENGTH));

                if (display((Property) entry.getValue(), out,
                                prefix + (i.hasNext() ? VERTICAL_LINE
                                                              : LAST_VERTICAL_LINE), false, true)
                            && !displayNewLine) {
                    out.println();
                    out.print(prefix);
                    out.print(EMPTY_LINE);
                }
            }
        }

        return displayNewLine;
    }

    private String toString(Object o, int length) {
        // 如果是null, 则返回字符串"null".
        if (o == null) {
            return "null";
        }

        // 如果是字符串, 则返回"...".
        // 否则返回{ClassName: "..."}.
        StringBuffer buffer = new StringBuffer();

        if (o instanceof String) {
            buffer.append('"');
        } else {
            int    c;
            String className = o.getClass().getName();

            if ((c = className.lastIndexOf('$')) != -1) {
                className = className.substring(c + 1);
            } else if ((c = className.lastIndexOf('.')) != -1) {
                className = className.substring(c + 1);
            }

            buffer.append('{').append(className).append(": \"");
        }

        String str = o.toString();

        for (int i = 0; (i < str.length()) && (i < length); i++) {
            char c = str.charAt(i);

            if (c == '\n') {
                buffer.append("\\n");
            } else if (c < ' ') {
                buffer.append('.');
            } else if (c == '"') {
                buffer.append("\\\"");
            } else if (c == '\\') {
                buffer.append("\\\\");
            } else {
                buffer.append(c);
            }
        }

        if (str.length() > length) {
            buffer.append("...(more)");
        }

        if (o instanceof String) {
            buffer.append('"');
        } else {
            buffer.append("\"}");
        }

        return buffer.toString();
    }

    /**
     * 取得不同类型的属性值                                               此处类型囊括了所有java的基本类型和BigDecimal,
     * BigInteger类
     */
    /**
     * 得到BigDecimal值.
     *
     * @return BigDecimal
     */
    public BigDecimal getBigDecimal() {
        return (BigDecimal) Convert.asType(BigDecimal.class, getValue());
    }

    /**
     * 得到BigDecimal值. 如果失败, 则取默认值.
     *
     * @param defaultValue 默认值
     *
     * @return BigDecimal
     */
    public BigDecimal getBigDecimal(BigDecimal defaultValue) {
        return (BigDecimal) Convert.asType(BigDecimal.class, getValue(), defaultValue);
    }

    /**
     * 得到BigInteger值.
     *
     * @return BigInteger
     */
    public BigInteger getBigInteger() {
        return (BigInteger) Convert.asType(BigInteger.class, getValue());
    }

    /**
     * 得到BigInteger值. 如果失败, 则取默认值.
     *
     * @param defaultValue 默认值
     *
     * @return BigInteger
     */
    public BigInteger getBigInteger(BigInteger defaultValue) {
        return (BigInteger) Convert.asType(BigInteger.class, getValue(), defaultValue);
    }

    /**
     * 得到boolean值.
     *
     * @return boolean
     */
    public boolean getBoolean() {
        return ((Boolean) Convert.asType(Boolean.class, getValue())).booleanValue();
    }

    /**
     * 得到boolean值. 如果失败, 则取默认值.
     *
     * @param defaultValue 默认值
     *
     * @return boolean
     */
    public boolean getBoolean(boolean defaultValue) {
        return ((Boolean) Convert.asType(Boolean.class, getValue(), new Boolean(defaultValue)))
        .booleanValue();
    }

    /**
     * 得到byte值.
     *
     * @return byte
     */
    public byte getByte() {
        return ((Byte) Convert.asType(Byte.class, getValue())).byteValue();
    }

    /**
     * 得到byte值. 如果失败, 则取默认值.
     *
     * @param defaultValue 默认值
     *
     * @return byte
     */
    public byte getByte(byte defaultValue) {
        return ((Byte) Convert.asType(Byte.class, getValue(), new Byte(defaultValue))).byteValue();
    }

    /**
     * 得到double.
     *
     * @return double
     */
    public double getDouble() {
        return ((Double) Convert.asType(Double.class, getValue())).doubleValue();
    }

    /**
     * 得到double值. 如果失败, 则取默认值.
     *
     * @param defaultValue 默认值
     *
     * @return double
     */
    public double getDouble(double defaultValue) {
        return ((Double) Convert.asType(Double.class, getValue(), new Double(defaultValue)))
        .doubleValue();
    }

    /**
     * 得到float.
     *
     * @return float
     */
    public float getFloat() {
        return ((Float) Convert.asType(Float.class, getValue())).floatValue();
    }

    /**
     * 得到float值. 如果失败, 则取默认值.
     *
     * @param defaultValue 默认值
     *
     * @return float
     */
    public float getFloat(float defaultValue) {
        return ((Float) Convert.asType(Float.class, getValue(), new Float(defaultValue)))
        .floatValue();
    }

    /**
     * 得到int.
     *
     * @return int
     */
    public int getInteger() {
        return ((Integer) Convert.asType(Integer.class, getValue())).intValue();
    }

    /**
     * 得到int值. 如果失败, 则取默认值.
     *
     * @param defaultValue 默认值
     *
     * @return int
     */
    public int getInteger(int defaultValue) {
        return ((Integer) Convert.asType(Integer.class, getValue(), new Integer(defaultValue)))
        .intValue();
    }

    /**
     * 得到long.
     *
     * @return long
     */
    public long getLong() {
        return ((Long) Convert.asType(Long.class, getValue())).longValue();
    }

    /**
     * 得到long值. 如果失败, 则取默认值.
     *
     * @param defaultValue 默认值
     *
     * @return long
     */
    public long getLong(long defaultValue) {
        return ((Long) Convert.asType(Long.class, getValue(), new Long(defaultValue))).longValue();
    }

    /**
     * 得到short.
     *
     * @return short
     */
    public short getShort() {
        return ((Short) Convert.asType(Short.class, getValue())).shortValue();
    }

    /**
     * 得到short值. 如果失败, 则取默认值.
     *
     * @param defaultValue 默认值
     *
     * @return short
     */
    public short getShort(short defaultValue) {
        return ((Short) Convert.asType(Short.class, getValue(), new Short(defaultValue)))
        .shortValue();
    }

    /**
     * 得到String值.
     *
     * @return String
     */
    public String getString() {
        return (String) Convert.asType(String.class, getValue());
    }

    /**
     * 得到String值. 如果失败, 则取默认值.
     *
     * @param defaultValue 默认值
     *
     * @return String
     */
    public String getString(String defaultValue) {
        return (String) Convert.asType(String.class, getValue(), defaultValue);
    }
}

