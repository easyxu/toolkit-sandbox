package com.phoenix.common.convert.converters;

import com.phoenix.common.convert.ConvertChain;
import com.phoenix.common.convert.ConvertFailedException;
import com.phoenix.common.convert.Converter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 将对象转换成布尔值.
 * 
 * <ul>
 * <li>
 * 如果对象为<code>null</code>, 则抛出带默认值的<code>ConvertFailedException</code>.
 * </li>
 * <li>
 * 如果对象已经是<code>Boolean</code>了, 直接返回.
 * </li>
 * <li>
 * 如果对象是<code>Number</code>类型, 且不为<code>0</code>, 则返回<code>true</code>, 否则返回<code>false</code>.
 * </li>
 * <li>
 * 如果对象是字符串, 且字符串看起来像一个整数, 且数值不为<code>0</code>, 则返回<code>true</code>, 否则返回<code>false</code>.
 * </li>
 * <li>
 * 如果对象为空字符串, 则抛出带默认值的<code>ConvertFailedException</code>.
 * </li>
 * <li>
 * 如果对象为下列值之一的字符串(大小写不敏感): <code>"false", "null", "nul", "nil", "off", "no", "n"</code>
 * 则为<code>false</code>.
 * </li>
 * <li>
 * 如果对象为下列值之一的字符串(大小写不敏感): <code>"true", "on", "yes", "y"</code> 则为<code>true</code>.
 * </li>
 * <li>
 * 如果对象是字符串, 且不符合上述所有条件, 则抛出<code>ConvertFailedException</code>.
 * </li>
 * <li>
 * 否则, 把对象传递给下一个<code>Converter</code>处理.
 * </li>
 * </ul>
 * 
 *
 * @author Michael Zhou
 * @version $Id: BooleanConverter.java 1334 2005-05-27 00:52:23Z baobao $
 */
@SuppressWarnings("unchecked")
public class BooleanConverter implements Converter {
    protected static final Boolean DEFAULT_VALUE = Boolean.FALSE;
    protected static final Set     TRUE_STRINGS = new HashSet(Arrays.asList(
                new String[] { "true", "on", "yes", "y" }));
    protected static final Set     FALSE_STRINGS = new HashSet(Arrays.asList(
                new String[] { "false", "null", "nul", "nil", "off", "no", "n" }));

    public Object convert(Object value, ConvertChain chain) {
        if (value == null) {
            throw new ConvertFailedException().setDefaultValue(DEFAULT_VALUE);
        }

        if (value instanceof Boolean) {
            return value;
        }

        if (value instanceof Number) {
            return (Math.abs(((Number) value).doubleValue()) < Float.MIN_VALUE) ? Boolean.FALSE
                                                                                : Boolean.TRUE;
        }

        if (value instanceof String) {
            String strValue = ((String) value).trim();

            try {
                return (Integer.parseInt(strValue) == 0) ? Boolean.FALSE
                                                                  : Boolean.TRUE;
            } catch (NumberFormatException e) {
                if (strValue.length() == 0) {
                    throw new ConvertFailedException().setDefaultValue(DEFAULT_VALUE);
                }

                strValue = strValue.toLowerCase();

                if (TRUE_STRINGS.contains(strValue)) {
                    return Boolean.TRUE;
                }

                if (FALSE_STRINGS.contains(strValue)) {
                    return Boolean.FALSE;
                }

                throw new ConvertFailedException(e).setDefaultValue(DEFAULT_VALUE);
            }
        }

        return chain.convert(value);
    }
}

