package com.phoenix.common.enums;

import com.phoenix.common.convert.ConvertChain;
import com.phoenix.common.convert.ConvertFailedException;
import com.phoenix.common.convert.Converter;

/**
 * 将对象转换成<code>Enum</code>.
 * 
 * <ul>
 * <li>
 * 如果对象已经是<code>targetType</code>了, 直接返回.
 * </li>
 * <li>
 * 如果对象是字符串, 则试着把它转换成字符串名称所代表的<code>Enum</code>.  如果成功, 则返回.
 * </li>
 * <li>
 * 试着将对象转换成<code>Enum.getUnderlyingClass</code>类型, 如果成功, 则返回对应的<code>Enum</code>.
 * </li>
 * <li>
 * 如果有默认值, 则抛出带默认值的<code>ConvertFailedException</code>
 * </li>
 * <li>
 * 否则, 把对象传递给下一个<code>Converter</code>处理.
 * </li>
 * </ul>
 * 
 *
 * @author Michael Zhou
 * @version $Id: EnumConverter.java 579 2004-02-16 05:42:07Z baobao $
 */
@SuppressWarnings("unchecked")
public class EnumConverter implements Converter {

	public Object convert(Object value, ConvertChain chain) {
        Class targetType = chain.getTargetType();

        if (targetType.isInstance(value)) {
            return value;
        }

        if (value instanceof String) {
            Enum enums = Enum.getEnumByName(targetType, ((String) value).trim());

            if (enums != null) {
                return enums;
            }
        }

        Enum enums = null;

        try {
            Object enumValue = chain.getConvertManager().asTypeWithoutDefaultValue(Enum
                    .getUnderlyingClass(targetType), value);

            enums = Enum.getEnumByValue(targetType, enumValue);
        } catch (ConvertFailedException e) {
            if (e.isDefaultValueSet()) {
                enums = Enum.getEnumByValue(targetType, e.getDefaultValue());

                if (enums != null) {
                    throw new ConvertFailedException(e).setDefaultValue(enums);
                }
            }

            return chain.convert(value);
        }

        if (enums == null) {
            throw new ConvertFailedException();
        }

        return enums;
    }
}
