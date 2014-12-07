package com.phoenix.common.convert;

/**
 * 如果对象实现了此接口, 则在类型转换过程中优先使用此对象指定的转换器.
 *
 * @author Michael Zhou
 * @version $Id: Convertible.java 509 2004-02-16 05:42:07Z baobao $
 */
public interface Convertible {
    /**
     * 取得指定目标类型的转换器.
     *
     * @param targetType 目标类型
     *
     * @return 转换器, 如果不存在合适的转换器, 则返回<code>null</code>
     */
    @SuppressWarnings("unchecked")
	Converter getConverter(Class targetType);
}
