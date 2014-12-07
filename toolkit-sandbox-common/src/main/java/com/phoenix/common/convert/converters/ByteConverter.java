package com.phoenix.common.convert.converters;

import com.phoenix.common.convert.ConvertChain;
import com.phoenix.common.convert.ConvertFailedException;
import com.phoenix.common.convert.Converter;

/**
 * 将对象转换成字节.
 * 
 * <ul>
 * <li>
 * 如果对象为<code>null</code>, 则抛出带默认值的<code>ConvertFailedException</code>.
 * </li>
 * <li>
 * 如果对象已经是<code>Byte</code>了, 直接返回.
 * </li>
 * <li>
 * 如果对象是<code>Number</code>类型, 则返回它的字节值.
 * </li>
 * <li>
 * 如果对象是空字符串, 则抛出带默认值的<code>ConvertFailedException</code>.
 * </li>
 * <li>
 * 如果对象是字符串, 则试着把它转换成字节.  如果不成功, 则抛出<code>ConvertFailedException</code>.
 * </li>
 * <li>
 * 否则, 把对象传递给下一个<code>Converter</code>处理.
 * </li>
 * </ul>
 * 
 *
 * @author Michael Zhou
 * @version $Id: ByteConverter.java 1334 2005-05-27 00:52:23Z baobao $
 */
public class ByteConverter implements Converter {
    protected static final Byte DEFAULT_VALUE = new Byte((byte) 0);

    public Object convert(Object value, ConvertChain chain) {
        if (value == null) {
            throw new ConvertFailedException().setDefaultValue(DEFAULT_VALUE);
        }

        if (value instanceof Byte) {
            return value;
        }

        if (value instanceof Number) {
            return new Byte(((Number) value).byteValue());
        }

        if (value instanceof String) {
            String strValue = ((String) value).trim();

            try {
                return Byte.valueOf(strValue);
            } catch (NumberFormatException e) {
                if (strValue.length() > 0) {
                    throw new ConvertFailedException(e).setDefaultValue(DEFAULT_VALUE);
                }

                throw new ConvertFailedException().setDefaultValue(DEFAULT_VALUE);
            }
        }

        return chain.convert(value);
    }
}

