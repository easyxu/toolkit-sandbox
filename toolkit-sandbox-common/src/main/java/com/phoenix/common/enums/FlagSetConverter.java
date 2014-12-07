package com.phoenix.common.enums;

import com.phoenix.common.convert.ConvertChain;
import com.phoenix.common.convert.ConvertFailedException;
import com.phoenix.common.convert.Converter;

/**
 * 将对象转换成<code>FlagSet</code>.
 * 
 * <ul>
 * <li>
 * 如果对象已经是<code>targetType</code>了, 直接返回.
 * </li>
 * <li>
 * 试着将对象转换成<code>FlagSet.getUnderlyingType</code>类型, 如果成功, 则返回对应的<code>FlagSet</code>.
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
 * @version $Id: FlagSetConverter.java 579 2004-02-16 05:42:07Z baobao $
 */
public class FlagSetConverter implements Converter {
    @SuppressWarnings("unchecked")
	public Object convert(Object value, ConvertChain chain) {
        Class targetType = chain.getTargetType();

        if (targetType.isInstance(value)) {
            return value;
        }

        if (!FlagSet.class.isAssignableFrom(targetType)) {
            return chain.convert(value);
        }

        FlagSet flagSet = null;

        try {
            flagSet = (FlagSet) targetType.newInstance();
        } catch (Exception e) {
            return new ConvertFailedException();
        }

        try {
            Object flagSetValue = chain.getConvertManager().asTypeWithoutDefaultValue(flagSet
                    .getUnderlyingClass(), value);

            flagSet.setValue(flagSetValue);
        } catch (ConvertFailedException e) {
            if (e.isDefaultValueSet()) {
                flagSet.setValue(e.getDefaultValue());
                throw new ConvertFailedException(e).setDefaultValue(flagSet);
            }

            return chain.convert(value);
        }

        return flagSet;
    }
}

