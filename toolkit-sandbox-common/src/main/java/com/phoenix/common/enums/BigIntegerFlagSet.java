package com.phoenix.common.enums;

import java.math.BigInteger;

import java.text.MessageFormat;

/**
 * 代表超长整数类型的<code>FlagSet</code>.
 *
 * @author Michael Zhou
 * @version $Id: BigIntegerFlagSet.java 579 2004-02-16 05:42:07Z baobao $
 */
public abstract class BigIntegerFlagSet extends FlagSet {
    private static final long serialVersionUID = 148888351760285748L;
    private BigInteger        value = BigInteger.ZERO;

    /**
     * 创建一个超长整数位集.
     *
     * @param enumClass 位集所代表的内部枚举类
     */
    @SuppressWarnings("unchecked")
	public BigIntegerFlagSet(Class enumClass) {
        super(enumClass);

        if (!BigIntegerEnum.class.isAssignableFrom(enumClass)) {
            throw new IllegalArgumentException(MessageFormat.format(EnumConstants.ILLEGAL_CLASS,
                    new Object[] { enumClass.getName(), BigIntegerEnum.class.getName() }));
        }
    }

    /**
     * 设置位集的值, 值的类型由<code>getUnderlyingClass()</code>确定.
     *
     * @param value 位集的值
     */
    public void setValue(Object value) {
        checkImmutable();

        if (value == null) {
            throw new NullPointerException(EnumConstants.FLAT_SET_VALUE_IS_NULL);
        }

        this.value = (BigInteger) value;
    }

    /**
     * 取得位集的值, 值的类型由<code>getUnderlyingClass()</code>确定.
     *
     * @return 位集的值
     */
    public Object getValue() {
        return value;
    }

    /**
     * 清除当前位集的全部位.
     *
     * @return 当前位集
     */
    public Flags clear() {
        checkImmutable();
        value = BigInteger.ZERO;
        return this;
    }

    /**
     * 测试当前位集的指定位, 等效于<code>and(flags) != 0</code>.
     *
     * @param flags 标志位
     *
     * @return 如果指定位被置位, 则返回<code>true</code>
     */
    public boolean test(Flags flags) {
        return !value.and(getFlagsValue(flags)).equals(BigInteger.ZERO);
    }

    /**
     * 测试当前位集的指定位, 等效于<code>and(flags) == flags</code>.
     *
     * @param flags 标志位
     *
     * @return 如果指定位被置位, 则返回<code>true</code>
     */
    public boolean testAll(Flags flags) {
        BigInteger testValue = getFlagsValue(flags);

        return value.and(testValue).equals(testValue);
    }

    /**
     * 对当前位集执行逻辑与操作.
     *
     * @param flags 标志位
     *
     * @return 当前位集
     */
    public Flags and(Flags flags) {
        BigIntegerFlagSet flagSet = (BigIntegerFlagSet) getFlagSetForModification();

        flagSet.value = flagSet.value.and(getFlagsValue(flags));
        return flagSet;
    }

    /**
     * 对当前位集执行逻辑非操作.
     *
     * @param flags 标志位
     *
     * @return 当前位集
     */
    public Flags andNot(Flags flags) {
        BigIntegerFlagSet flagSet = (BigIntegerFlagSet) getFlagSetForModification();

        flagSet.value = flagSet.value.andNot(getFlagsValue(flags));
        return flagSet;
    }

    /**
     * 对当前位集执行逻辑或操作.
     *
     * @param flags 标志位
     *
     * @return 当前位集
     */
    public Flags or(Flags flags) {
        BigIntegerFlagSet flagSet = (BigIntegerFlagSet) getFlagSetForModification();

        flagSet.value = flagSet.value.or(getFlagsValue(flags));
        return flagSet;
    }

    /**
     * 对当前位集执行逻辑异或操作.
     *
     * @param flags 标志位
     *
     * @return 当前位集
     */
    public Flags xor(Flags flags) {
        BigIntegerFlagSet flagSet = (BigIntegerFlagSet) getFlagSetForModification();

        flagSet.value = flagSet.value.xor(getFlagsValue(flags));
        return flagSet;
    }

    /**
     * 取得位集的值.
     *
     * @param flags 位集
     *
     * @return 位集的值
     */
    private BigInteger getFlagsValue(Flags flags) {
        checkFlags(flags);
        return (flags instanceof BigIntegerEnum) ? (BigInteger) ((BigIntegerEnum) flags).getValue()
                                                 : ((BigIntegerFlagSet) flags).value;
    }

    /**
     * 实现<code>Number</code>类, 取得整数值.
     *
     * @return 整数值
     */
    public int intValue() {
        return value.intValue();
    }

    /**
     * 实现<code>Number</code>类, 取得长整数值.
     *
     * @return 长整数值
     */
    public long longValue() {
        return (long) value.longValue();
    }

    /**
     * 实现<code>Number</code>类, 取得<code>double</code>值.
     *
     * @return <code>double</code>值
     */
    public double doubleValue() {
        return (double) value.doubleValue();
    }

    /**
     * 实现<code>Number</code>类, 取得<code>float</code>值.
     *
     * @return <code>float</code>值
     */
    public float floatValue() {
        return (float) value.floatValue();
    }

    /**
     * 实现<code>IntegralNumber</code>类, 转换成十六进制整数字符串.
     *
     * @return 十六进制整数字符串
     */
    public String toHexString() {
        return value.toString(RADIX_HEX);
    }

    /**
     * 实现<code>IntegralNumber</code>类, 转换成八进制整数字符串.
     *
     * @return 八进制整数字符串
     */
    public String toOctalString() {
        return value.toString(RADIX_OCT);
    }

    /**
     * 实现<code>IntegralNumber</code>类, 转换成二进制整数字符串.
     *
     * @return 二进制整数字符串
     */
    public String toBinaryString() {
        return value.toString(RADIX_BIN);
    }
}

