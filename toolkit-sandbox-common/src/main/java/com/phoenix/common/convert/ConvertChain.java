package com.phoenix.common.convert;

/**
 * <p>
 * 代表一个转换链.
 * </p>
 * 
 * <p>
 * 将一个对象转换成指定类型时, 转换链中包括多个可能可以实现这个转换的所有转换器. 实行转换时, 如果前一个转换器不能转换这个value, 则将控制交给下一个转换器.
 * </p>
 *
 * @author Michael Zhou
 * @version $Id: ConvertChain.java 509 2004-02-16 05:42:07Z baobao $
 */
public interface ConvertChain {
    /**
     * 取得创建此链的<code>ConvertManager</code>.
     *
     * @return 创建此链的<code>ConvertManager</code>
     */
    ConvertManager getConvertManager();

    /**
     * 取得转换的目标类型.
     *
     * @return 目标类型
     */
    @SuppressWarnings("unchecked")
	Class getTargetType();

    /**
     * 将控制交给链中的下一个转换器, 转换指定的值到指定的类型.
     *
     * @param value 要转换的值
     *
     * @return 转换的结果
     */
    Object convert(Object value);
}
