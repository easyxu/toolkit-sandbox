package com.phoenix.common.convert;

/**
 * 将对象变成<code>Convertible</code>的包装器.
 *
 * @author Michael Zhou
 * @version $Id: ConvertibleWrapper.java 509 2004-02-16 05:42:07Z baobao $
 */
@SuppressWarnings("unchecked")
public abstract class ConvertibleWrapper implements Convertible {
    private Object wrappedObject;

    /**
     * 创建包装器.
     *
     * @param wrappedObject 被包装的对象
     */
    public ConvertibleWrapper(Object wrappedObject) {
        this.wrappedObject = wrappedObject;
    }

    /**
     * 取得被包装的对象.
     *
     * @return 被包装的对象
     */
    public Object getWrappedObject() {
        return wrappedObject;
    }

    /**
     * 取得<code>Converter</code>.
     *
     * @param targetType 目标类型
     *
     * @return 转换器<code>Converter</code>
     */
	public Converter getConverter(Class targetType) {
        return new Converter() {
                public Object convert(Object value, ConvertChain chain) {
                    Class targetType = chain.getTargetType();

                    value = preConvert(wrappedObject, targetType);
                    return postConvert(targetType, chain.convert(value));
                }
            };
    }

    /**
     * 预转换.
     *
     * @param wrappedObject 被包装的对象
     * @param targetType 目标类型
     *
     * @return 预转换后的对象
     */
    protected Object preConvert(Object wrappedObject, Class targetType) {
        return wrappedObject;
    }

    /**
     * 后转换.
     *
     * @param targetType 目标类型
     * @param convertedValue 转换的结果
     *
     * @return 经过处理的转换结果
     */
    protected Object postConvert(Class targetType, Object convertedValue) {
        return convertedValue;
    }
}
