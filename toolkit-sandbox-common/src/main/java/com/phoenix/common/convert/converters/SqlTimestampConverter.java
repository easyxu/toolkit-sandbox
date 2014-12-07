package com.phoenix.common.convert.converters;

import com.phoenix.common.convert.ConvertChain;
import com.phoenix.common.convert.ConvertFailedException;
import com.phoenix.common.convert.Converter;

import java.sql.Timestamp;

/**
 * 将对象转换成<code>java.sql.Timestamp</code>.
 * 
 * <ul>
 * <li>
 * 如果对象为<code>null</code>, 则抛出带默认值的<code>ConvertFailedException</code>.
 * </li>
 * <li>
 * 如果对象已经是<code>java.sql.Timestamp</code>了, 直接返回.
 * </li>
 * <li>
 * 如果对象是空字符串, 则抛出带默认值的<code>ConvertFailedException</code>.
 * </li>
 * <li>
 * 如果对象是字符串, 则试着把它转换成<code>java.sql.Timestamp</code>, 格式必须为"yyyy-mm-dd hh:mm:ss.fffffffff".  如果不成功,
 * 则抛出<code>ConvertFailedException</code>.
 * </li>
 * <li>
 * 否则, 把对象传递给下一个<code>Converter</code>处理.
 * </li>
 * </ul>
 * 
 *
 * @author Michael Zhou
 * @version $Id: SqlTimestampConverter.java 509 2004-02-16 05:42:07Z baobao $
 */
public class SqlTimestampConverter implements Converter {
    protected static final Timestamp DEFAULT_VALUE = null;

    public Object convert(Object value, ConvertChain chain) {
        if (value == null) {
            throw new ConvertFailedException().setDefaultValue(DEFAULT_VALUE);
        }

        if (value instanceof Timestamp) {
            return value;
        }

        if (value instanceof String) {
            String strValue = ((String) value).trim();

            try {
                return Timestamp.valueOf(strValue);
            } catch (IllegalArgumentException e) {
                if (strValue.length() > 0) {
                    throw new ConvertFailedException(e).setDefaultValue(DEFAULT_VALUE);
                }

                throw new ConvertFailedException().setDefaultValue(DEFAULT_VALUE);
            }
        }

        return chain.convert(value);
    }
}
