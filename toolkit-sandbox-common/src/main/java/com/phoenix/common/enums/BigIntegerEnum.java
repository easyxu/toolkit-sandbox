package com.phoenix.common.enums;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 类型安全的枚举类型, 代表一个超长整数.
 *
 * @author Michael Zhou
 * @version $Id: BigIntegerEnum.java 579 2004-02-16 05:42:07Z baobao $
 */
public abstract class BigIntegerEnum extends Enum {


    /**
     * 创建一个枚举量.
     *
     * @param name 枚举量的名称
     */
    protected BigIntegerEnum(String name) {
        super(name);
    }

    /**
     * 创建一个枚举量.
     *
     * @param name 枚举量的名称
     * @param value 枚举量的超长整数值
     */
    protected BigIntegerEnum(String name, int value) {
        super(name, new BigInteger(String.valueOf(value)));
    }

    /**
     * 创建一个枚举量.
     *
     * @param name 枚举量的名称
     * @param value 枚举量的超长整数值
     */
    protected BigIntegerEnum(String name, long value) {
        super(name, new BigInteger(String.valueOf(value)));
    }

    /**
     * 创建一个枚举量.
     *
     * @param name 枚举量的名称
     * @param value 枚举量的超长整数值
     */
    protected BigIntegerEnum(String name, String value) {
        super(name, new BigInteger(value));
    }

    /**
     * 创建一个枚举量.
     *
     * @param name 枚举量的名称
     * @param value 枚举量的超长整数值
     */
    protected BigIntegerEnum(String name, BigInteger value) {
        super(name, value);
    }

    /**
     * 创建一个枚举量.
     *
     * @param name 枚举量的名称
     * @param value 枚举量的超长整数值
     */
    protected BigIntegerEnum(String name, BigDecimal value) {
        super(name, value.toBigInteger());
    }

    /**
     * 创建一个枚举类型的<code>EnumType</code>.
     *
     * @return 枚举类型的<code>EnumType</code>
     */
    protected static Object createEnumType() {
        return new EnumType() {
                @SuppressWarnings("unchecked")
				protected Class getUnderlyingClass() {
                    return BigInteger.class;
                }

                protected Object getNextValue(Object value, boolean flagMode) {
                    if (value == null) {
                        return flagMode ? BigInteger.ONE
                                        : BigInteger.ZERO; // 默认起始值
                    }

                    if (flagMode) {
                        return ((BigInteger) value).shiftLeft(1); // 位模式
                    } else {
                        return ((BigInteger) value).add(BigInteger.ONE);
                    }
                }

                protected boolean isZero(Object value) {
                    return ((BigInteger) value).equals(BigInteger.ZERO);
                }

                protected boolean isPowerOfTwo(Object value) {
                    BigInteger bintValue = (BigInteger) value;
                    int        bitIndex = bintValue.getLowestSetBit();

                    if (bitIndex < 0) {
                        return false;
                    }

                    return bintValue.clearBit(bitIndex).equals(BigInteger.ZERO);
                }
            };
    }

    /**
     * 实现<code>Number</code>类, 取得整数值.
     *
     * @return 整数值
     */
    public int intValue() {
        return ((BigInteger) getValue()).intValue();
    }

    /**
     * 实现<code>Number</code>类, 取得长整数值.
     *
     * @return 长整数值
     */
    public long longValue() {
        return ((BigInteger) getValue()).longValue();
    }

    /**
     * 实现<code>Number</code>类, 取得<code>double</code>值.
     *
     * @return <code>double</code>值
     */
    public double doubleValue() {
        return ((BigInteger) getValue()).doubleValue();
    }

    /**
     * 实现<code>Number</code>类, 取得<code>float</code>值.
     *
     * @return <code>float</code>值
     */
    public float floatValue() {
        return ((BigInteger) getValue()).floatValue();
    }

    /**
     * 实现<code>IntegralNumber</code>类, 转换成十六进制整数字符串.
     *
     * @return 十六进制整数字符串
     */
    public String toHexString() {
        return ((BigInteger) getValue()).toString(RADIX_HEX);
    }

    /**
     * 实现<code>IntegralNumber</code>类, 转换成八进制整数字符串.
     *
     * @return 八进制整数字符串
     */
    public String toOctalString() {
        return ((BigInteger) getValue()).toString(RADIX_OCT);
    }

    /**
     * 实现<code>IntegralNumber</code>类, 转换成二进制整数字符串.
     *
     * @return 二进制整数字符串
     */
    public String toBinaryString() {
        return ((BigInteger) getValue()).toString(RADIX_BIN);
    }
}
