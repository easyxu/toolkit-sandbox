package com.phoenix.common.convert;

/**
 * 转换对象类型的工具类, 支持所有primitive类型和数组的转换.
 *
 * @author Michael Zhou
 * @version $Id: Convert.java 509 2004-02-16 05:42:07Z baobao $
 */
public class Convert {
    private static final ConvertManager defaultConvertManager = new ConvertManager();

    public static ConvertManager getDefaultConvertManager() {
        return defaultConvertManager;
    }

    /**
     * 将指定值转换成<code>boolean</code>类型.
     *
     * @param value 要转换的值
     *
     * @return 转换后的值
     */
    public static boolean asBoolean(Object value) {
        return defaultConvertManager.asBoolean(value);
    }

    /**
     * 将指定值转换成<code>boolean</code>类型.
     *
     * @param value 要转换的值
     * @param defaultValue 默认值
     *
     * @return 转换后的值
     */
    public static boolean asBoolean(Object value, boolean defaultValue) {
        return defaultConvertManager.asBoolean(value, defaultValue);
    }

    /**
     * 将指定值转换成<code>byte</code>类型.
     *
     * @param value 要转换的值
     *
     * @return 转换后的值
     */
    public static byte asByte(Object value) {
        return defaultConvertManager.asByte(value);
    }

    /**
     * 将指定值转换成<code>byte</code>类型.
     *
     * @param value 要转换的值
     * @param defaultValue 默认值
     *
     * @return 转换后的值
     */
    public static byte asByte(Object value, byte defaultValue) {
        return defaultConvertManager.asByte(value, defaultValue);
    }

    /**
     * 将指定值转换成<code>char</code>类型.
     *
     * @param value 要转换的值
     *
     * @return 转换后的值
     */
    public static char asChar(Object value) {
        return defaultConvertManager.asChar(value);
    }

    /**
     * 将指定值转换成<code>char</code>类型.
     *
     * @param value 要转换的值
     * @param defaultValue 默认值
     *
     * @return 转换后的值
     */
    public static char asChar(Object value, char defaultValue) {
        return defaultConvertManager.asChar(value, defaultValue);
    }

    /**
     * 将指定值转换成<code>double</code>类型.
     *
     * @param value 要转换的值
     *
     * @return 转换后的值
     */
    public static double asDouble(Object value) {
        return defaultConvertManager.asDouble(value);
    }

    /**
     * 将指定值转换成<code>double</code>类型.
     *
     * @param value 要转换的值
     * @param defaultValue 默认值
     *
     * @return 转换后的值
     */
    public static double asDouble(Object value, double defaultValue) {
        return defaultConvertManager.asDouble(value, defaultValue);
    }

    /**
     * 将指定值转换成<code>float</code>类型.
     *
     * @param value 要转换的值
     *
     * @return 转换后的值
     */
    public static float asFloat(Object value) {
        return defaultConvertManager.asFloat(value);
    }

    /**
     * 将指定值转换成<code>float</code>类型.
     *
     * @param value 要转换的值
     * @param defaultValue 默认值
     *
     * @return 转换后的值
     */
    public static float asFloat(Object value, float defaultValue) {
        return defaultConvertManager.asFloat(value, defaultValue);
    }

    /**
     * 将指定值转换成<code>int</code>类型.
     *
     * @param value 要转换的值
     *
     * @return 转换后的值
     */
    public static int asInt(Object value) {
        return defaultConvertManager.asInt(value);
    }

    /**
     * 将指定值转换成<code>int</code>类型.
     *
     * @param value 要转换的值
     * @param defaultValue 默认值
     *
     * @return 转换后的值
     */
    public static int asInt(Object value, int defaultValue) {
        return defaultConvertManager.asInt(value, defaultValue);
    }

    /**
     * 将指定值转换成<code>long</code>类型.
     *
     * @param value 要转换的值
     *
     * @return 转换后的值
     */
    public static long asLong(Object value) {
        return defaultConvertManager.asLong(value);
    }

    /**
     * 将指定值转换成<code>long</code>类型.
     *
     * @param value 要转换的值
     * @param defaultValue 默认值
     *
     * @return 转换后的值
     */
    public static long asLong(Object value, long defaultValue) {
        return defaultConvertManager.asLong(value, defaultValue);
    }

    /**
     * 将指定值转换成<code>short</code>类型.
     *
     * @param value 要转换的值
     *
     * @return 转换后的值
     */
    public static short asShort(Object value) {
        return defaultConvertManager.asShort(value);
    }

    /**
     * 将指定值转换成<code>short</code>类型.
     *
     * @param value 要转换的值
     * @param defaultValue 默认值
     *
     * @return 转换后的值
     */
    public static short asShort(Object value, short defaultValue) {
        return defaultConvertManager.asShort(value, defaultValue);
    }

    /**
     * 将指定值转换成<code>String</code>类型.
     *
     * @param value 要转换的值
     *
     * @return 转换后的值
     */
    public static String asString(Object value) {
        return defaultConvertManager.asString(value);
    }

    /**
     * 将指定值转换成<code>String</code>类型.
     *
     * @param value 要转换的值
     * @param defaultValue 默认值
     *
     * @return 转换后的值
     */
    public static String asString(Object value, String defaultValue) {
        return defaultConvertManager.asString(value, defaultValue);
    }

    /**
     * 将指定值转换成指定类型.
     *
     * @param targetType 要转换的目标类型
     * @param value 要转换的值
     *
     * @return 转换后的值
     */
    public static Object asType(Object targetType, Object value) {
        return defaultConvertManager.asType(targetType, value);
    }

    /**
     * 将指定值转换成指定类型.
     *
     * @param targetType 要转换的目标类型
     * @param value 要转换的值
     * @param defaultValue 默认值
     *
     * @return 转换后的值
     */
    public static Object asType(Object targetType, Object value, Object defaultValue) {
        return defaultConvertManager.asType(targetType, value, defaultValue);
    }
}
