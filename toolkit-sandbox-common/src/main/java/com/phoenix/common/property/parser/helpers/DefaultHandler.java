package com.phoenix.common.property.parser.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phoenix.common.property.Property;
import com.phoenix.common.property.parser.Locator;
import com.phoenix.common.property.parser.ParseException;
import com.phoenix.common.property.parser.ParseHandler;

@SuppressWarnings("unchecked")
public class DefaultHandler implements ParseHandler {
    /** Handler记录器. */
    protected static Logger log = LoggerFactory.getLogger("property.parser.ParseHandler");

    /** 状态常量. */
    private static final int IN_SET_PROPERTY = 1;
    private static final int IN_BASE         = 2;
    private static final int IN_REFERENCE    = 3;
    private static final int IN_GET_PROPERTY = 4;

    /** 特殊含义的变量. */
    private static final String VAR_ROOT   = "root";
    private static final String VAR_PARENT = "parent";
    private static final String VAR_BASE   = "base";

    /** 当前状态. */
    private int curState;

    /** 属性计数. 通常第一个属性和后面的属性的行为会有所不同. */
    private int propertyCount;

    /** 记录行号列号的对象. */
    private Locator locator;

    /** 所有的结点. */
    private List allNodes;

    /** 结点对象. */
    private Node root;

    /** 结点对象. */
    private Node base;

    /** 结点对象. */
    private Node self;

    /** 结点对象. */
    private Node current;

    /** 重要! 一个结点可以对应多个property对象, 所以要保存以下property对象. */
    private Property rootProperty;

    /** 重要! 一个结点可以对应多个property对象, 所以要保存以下property对象. */
    private Property baseProperty;

    /**
     * 设置行号列号信息的对象.
     */
    public void setLocator(Locator locator) {
        this.locator = locator;
    }

    /**
     * 取得根属性.
     */
    public Property getRootProperty() {
        return rootProperty;
    }

    /**
     * 开始解析, 创建根结点.
     */
	public void start() throws ParseException {
        allNodes         = new ArrayList();
        rootProperty     = baseProperty = new Property("root");
        root             = base = getNode(rootProperty);
    }

    /**
     * 结束解析. 遍历所有结点, 对结点求值, 检查引用的依赖性.
     */
    public void end() throws ParseException {
        for (Iterator i = allNodes.iterator(); i.hasNext();) {
            setNodeValue((Node) i.next());
        }

        rootProperty.tidy();
    }

    private void setNodeValue(Node node) {
        if (node.initialized) {
            if (node.property.getValue() instanceof Node) {
                if (node.values == null) {
                    node.property.setValue(node.value);
                } else {
                    StringBuffer buffer = new StringBuffer();

                    for (Iterator i = node.values.iterator(); i.hasNext();) {
                        Object value = i.next();

                        if (value instanceof Node) {
                            setNodeValue((Node) value);
                            value = ((Node) value).property.getValue();
                        }

                        buffer.append((value == null) ? ""
                                                      : value);
                    }

                    buffer.append(node.value);
                    node.property.setValue(buffer.toString());
                }
            }
        } else {
            node.property.setValue(null);
        }
    }

    /**
     * 设置根结点的名称(键值).
     */
    public void setName(String name) throws ParseException {
        rootProperty.setKey(name);
    }

    /**
     * 从资源中载入另一个属性文件并解析之.
     */
    public void include(String resource) throws ParseException {
    }

    /**
     * 开始解析base命令.
     */
    public void startBase() throws ParseException {
        curState          = IN_BASE;
        propertyCount     = 0;
    }

    /**
     * 结束解析base命令.  如果省略property, 则将base设成root.
     */
    public void endBase() throws ParseException {
        if (propertyCount == 0) {
            base             = root;
            baseProperty     = rootProperty;
        }
    }

    /**
     * 开始解析一对属性和值.
     */
    public void startPropertyDefinition() throws ParseException {
        curState          = IN_SET_PROPERTY;
        propertyCount     = 0;
    }

    /**
     * 结束一对属性和值的解析.
     */
    public void endPropertyDefinition() throws ParseException {
    }

    /**
     * 开始解析一个属性.
     */
    public void startProperty() throws ParseException {
        switch (curState) {
            case IN_BASE:
                current = root;
                current.property = rootProperty;
                break;

            case IN_SET_PROPERTY:
            case IN_REFERENCE:
                current = base;
                current.property = baseProperty;
                break;

            case IN_GET_PROPERTY:}
    }

    /**
     * 结束解析一个属性.
     */
    public void endProperty() throws ParseException {
        switch (curState) {
            case IN_SET_PROPERTY:
                self = current;
                break;

            case IN_BASE:
                base = current;
                baseProperty = current.property;
                break;

            case IN_REFERENCE:

                if (self.equals(current)
                            || ((self.dependency != null) && self.dependency.contains(current))) {
                    throw new ParseException("Circular reference detected" + " at line "
                        + locator.getLineNumber() + ", column " + locator.getColumnNumber() + ".");
                }

                if (current.dependency == null) {
                    current.dependency = new HashSet();
                }

                current.dependency.add(self);
                self.values.add(current);
                break;

            case IN_GET_PROPERTY:}
    }

    /**
     * 解释一个特殊变量, 如root, parent, base等.
     */
    public void propertyVar(String var) throws ParseException {
        switch (curState) {
            case IN_BASE:
            case IN_REFERENCE:

                if (propertyCount == 0) {
                    if (VAR_ROOT.equals(var)) {
                        current              = root;
                        current.property     = rootProperty;
                    } else if (VAR_BASE.equals(var)) {
                        current              = base;
                        current.property     = baseProperty;
                    } else if (VAR_PARENT.equals(var)) {
                        if (base.parent == null) {
                            throw new ParseException("Can't get parent of root property "
                                + " at line " + locator.getLineNumber() + ", column "
                                + locator.getColumnNumber() + ".");
                        } else {
                            current              = base.parent;
                            current.property     = current.startProperty;
                        }
                    } else {
                        throw new ParseException("No variable \"" + var + "\" available here"
                            + " at line " + locator.getLineNumber() + ", column "
                            + locator.getColumnNumber() + ".");
                    }
                } else {
                    if (VAR_PARENT.equals(var)) {
                        if (current.parent == null) {
                            throw new ParseException("Can't get parent of root property "
                                + " at line " + locator.getLineNumber() + ", column "
                                + locator.getColumnNumber() + ".");
                        } else {
                            current              = current.parent;
                            current.property     = current.startProperty;
                        }
                    } else {
                        throw new ParseException("No variable \"" + var + "\" available here"
                            + " at line " + locator.getLineNumber() + ", column "
                            + locator.getColumnNumber() + ".");
                    }
                }

                break;

            case IN_SET_PROPERTY:
            case IN_GET_PROPERTY:default:
                throw new ParseException("No variable \"" + var + "\" available here" + " at line "
                    + locator.getLineNumber() + ", column " + locator.getColumnNumber() + ".");
        }

        propertyCount++;
    }

    /**
     * 解析到一个属性key.
     */
    public void propertyKey(String key) throws ParseException {
        switch (curState) {
            case IN_SET_PROPERTY:
            case IN_BASE:
            case IN_REFERENCE:

                Node parent = current;

                current = getNode(current.property.ensureChild(key));
                current.parent = parent;
                break;

            case IN_GET_PROPERTY:}

        propertyCount++;
    }

    /**
     * 解析到一个属性索引. 索引-1表示在文件中未指明具体的索引值.
     */
    public void propertyIndex(int index) throws ParseException {
        switch (curState) {
            case IN_SET_PROPERTY:
            case IN_BASE:
            case IN_REFERENCE:

                Node parent = current;

                if (index >= 0) {
                    current = getNode(current.property.ensureIndex(index));
                } else if ((index == -1) && (curState != IN_REFERENCE)) {
                    if (current.property.isList()) {
                        current = getNode(current.property.ensureIndex(current.property.size()));
                    } else {
                        if (current.initialized) {
                            current = getNode(current.property.ensureIndex(1));
                        } else {
                            current = getNode(current.property.ensureIndex(0));
                        }
                    }
                } else {
                    if (index == -1) {
                        throw new ParseException("Missed index" + " at line "
                            + locator.getLineNumber() + ", column " + locator.getColumnNumber()
                            + ".");
                    } else {
                        throw new ParseException("Invalid index " + index + " at line "
                            + locator.getLineNumber() + ", column " + locator.getColumnNumber()
                            + ".");
                    }
                }

                // 重要! current.parent是指非List型的祖先, 所以新产生的结点的parent与原结点是一样的.
                current.parent = parent.parent;
                break;

            case IN_GET_PROPERTY:}

        propertyCount++;
    }

    /**
     * 设置值.
     */
    public void value(String value) throws ParseException {
        if (self.initialized) {
            if (self.property.isList()) {
                current = getNode(self.property.ensureIndex(self.property.size()));
            } else {
                current = getNode(self.property.ensureIndex(1));
            }
        } else {
            current = self;
        }

        current.value           = value;
        current.initialized     = true;
    }

    /**
     * 开始引用.
     */
    public void startReference(String valuePart) throws ParseException {
        curState          = IN_REFERENCE;
        propertyCount     = 0;

        if (self.values == null) {
            self.values = new ArrayList();
        }

        self.values.add(valuePart);
    }

    /**
     * 结束引用.
     */
    public void endReference() throws ParseException {
    }

    /**
     * 取得指定属性的结点.
     */
    private Node getNode(Property p) {
        Node node;

        if (p.getValue() == null) {
            node = new Node();
            p.setValue(node);
            node.startProperty = p;
            allNodes.add(node);
        } else {
            node = (Node) p.getValue();
        }

        // 重要! 更新node中的property指向当前操作的Property对象.
        node.property = p;
        return node;
    }

    /**
     * 在创建Property树的过程中需要很多附加的信息, 所以创建Node来存放这些信息.
     * 另一个使用Node类的原因是Property类的hashCode方法的值是根据Property中的值而 改变的, 所以不能直接用Property对象来作为hash表的键值.
     * 每个Property对应一个 Node, Node中也保存着这个Property对象的引用.
     */
    private static class Node {
        public String toString() {
            return ((values == null) ? ""
                                     : (values.toString() + "; "))
            + ((value == null) ? ""
                               : value.toString());
        }

        /** Node对应的property. */
        Property property;

        /** Node对应的第一个property. 由于一个Node可以对应多个property. 假设第一个为起点. */
        Property startProperty;

        /** Node是否初始化过(有值). */
        boolean initialized;

        /** 直接父结点. */
        Node parent;

        /** 所有依赖于这个结点的结点集合. */
        Set dependency;

        /** 所有值的部分. */
        List values;

        /** 最后一个部分值. */
        String value;
    }
}

