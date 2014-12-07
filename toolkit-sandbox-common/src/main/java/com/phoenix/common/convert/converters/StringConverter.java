package com.phoenix.common.convert.converters;

import com.phoenix.common.convert.ConvertChain;
import com.phoenix.common.convert.ConvertFailedException;
import com.phoenix.common.convert.Converter;

/**
 * 将对象转换成字符串.
 * 
 * <ul>
 * <li>
 * 如果对象为<code>null</code>, 则抛出带默认值的<code>ConvertFailedException</code>.
 * </li>
 * <li>
 * 如果对象已经是字符串, 直接返回.
 * </li>
 * <li>
 * 如果对象是字符数组, 则将它组合成字符串.
 * </li>
 * <li>
 * 否则返回<code>toString()</code>.
 * </li>
 * </ul>
 * 
 *
 * @author Michael Zhou
 * @version $Id: StringConverter.java 509 2004-02-16 05:42:07Z baobao $
 */
public class StringConverter implements Converter {
    protected static final String DEFAULT_VALUE = "";

    public Object convert(Object value, ConvertChain chain) {
        if (value == null) {
            throw new ConvertFailedException().setDefaultValue(DEFAULT_VALUE);
        }

        if (value instanceof String) {
            return value;
        }

        if (value.getClass().isArray()) {
            if (value.getClass().getComponentType().equals(Character.TYPE)) {
                return new String((char[]) value);
            }
        }

        return value.toString();
    }
}
