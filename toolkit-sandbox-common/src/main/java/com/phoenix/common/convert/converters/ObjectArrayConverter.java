package com.phoenix.common.convert.converters;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import com.phoenix.common.collection.EnumerationIterator;
import com.phoenix.common.convert.ConvertChain;
import com.phoenix.common.convert.ConvertFailedException;
import com.phoenix.common.convert.ConvertManager;
import com.phoenix.common.convert.Converter;

/**
 * 将对象转换成对象数组.
 * 
 * <ul>
 * <li>
 * 如果<code>targetType</code>不是数组类型, 则抛出<code>ConvertFailedException</code>.
 * </li>
 * <li>
 * 如果对象为<code>null</code>, 则抛出带默认值的<code>ConvertFailedException</code>.
 * </li>
 * <li>
 * 如果对象已经是<code>targetType</code>了, 直接返回.
 * </li>
 * <li>
 * 如果对象为数组, 则遍历数组, 对于每个数组元素, 执行转换.
 * </li>
 * <li>
 * 如果对象为<code>Collection</code>, 则遍历之, 对于每个集合元素, 执行转换.
 * </li>
 * <li>
 * 如果对象为<code>Iterator</code>或<code>Enumeration</code>, 则遍历之, 对于每个元素, 执行转换.
 * </li>
 * <li>
 * 否则, 把对象传递给下一个<code>Converter</code>处理.
 * </li>
 * </ul>
 * 
 *
 * @author Michael Zhou
 * @version $Id: ObjectArrayConverter.java 509 2004-02-16 05:42:07Z baobao $
 */
public class ObjectArrayConverter implements Converter {
    @SuppressWarnings("unchecked")
	public Object convert(Object value, ConvertChain chain) {
        Class targetType    = chain.getTargetType();
        Class componentType = targetType.getComponentType();

        if (componentType == null) {
            throw new ConvertFailedException();
        }

        if (value == null) {
            throw new ConvertFailedException().setDefaultValue(Array.newInstance(componentType, 0));
        }

        if (targetType.isInstance(value)) {
            return value;
        }

        if (value.getClass().isArray()) {
            int            length          = Array.getLength(value);
            Object         convertedValues = Array.newInstance(componentType, length);
            ConvertManager manager         = chain.getConvertManager();

            for (int i = 0; i < length; i++) {
                Array.set(convertedValues, i, manager.asType(componentType, Array.get(value, i)));
            }

            return convertedValues;
        }

        if (value instanceof Collection) {
            Collection     values          = (Collection) value;
            Object         convertedValues = Array.newInstance(componentType, values.size());
            ConvertManager manager         = chain.getConvertManager();

            Iterator       iterator = values.iterator();

            for (int i = 0; iterator.hasNext(); i++) {
                Array.set(convertedValues, i, manager.asType(componentType, iterator.next()));
            }

            return convertedValues;
        }

        Iterator iterator = null;

        if (value instanceof Iterator) {
            iterator = (Iterator) value;
        } else if (value instanceof Enumeration) {
            iterator = new EnumerationIterator((Enumeration) value);
        }

        if (iterator != null) {
            List           convertedValueList = new ArrayList();
            ConvertManager manager = chain.getConvertManager();

            while (iterator.hasNext()) {
                convertedValueList.add(manager.asType(componentType, iterator.next()));
            }

            int    length          = convertedValueList.size();
            Object convertedValues = Array.newInstance(componentType, length);

            for (int i = 0; i < length; i++) {
                Array.set(convertedValues, i, convertedValueList.get(i));
            }

            return convertedValues;
        }

        return chain.convert(value);
    }
}
