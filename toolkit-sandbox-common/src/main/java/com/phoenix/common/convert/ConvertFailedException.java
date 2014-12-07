package com.phoenix.common.convert;

import com.phoenix.exceptions.ChainedRuntimeException;

/**
 * 表示转换失败的异常.  转换失败时, 可以指定一个建议的默认值.
 *
 * @author Michael Zhou
 * @version $Id: ConvertFailedException.java 1291 2005-03-04 03:23:30Z baobao $
 */
public class ConvertFailedException extends ChainedRuntimeException {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -1208317713410434610L;
	private Object  defaultValue;
    private boolean defaultValueSet = false;

    /**
     * 构造一个空的异常.
     */
    public ConvertFailedException() {
        super();
    }

    /**
     * 构造一个异常, 指明异常的详细信息.
     *
     * @param message 详细信息
     */
    public ConvertFailedException(String message) {
        super(message);
    }

    /**
     * 构造一个异常, 指明引起这个异常的起因.
     *
     * @param cause 异常的起因
     */
    public ConvertFailedException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造一个异常, 指明引起这个异常的起因.
     *
     * @param message 详细信息
     * @param cause 异常的起因
     */
    public ConvertFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 设置建议的默认值.
     *
     * @param defaultValue 默认值
     *
     * @return 异常本身
     */
    public ConvertFailedException setDefaultValue(Object defaultValue) {
        this.defaultValue        = defaultValue;
        this.defaultValueSet     = true;
        return this;
    }

    /**
     * 取得默认值.
     *
     * @return 默认值对象
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * 是否设置了默认值.
     *
     * @return 如果设置了默认值, 则返回<code>true</code>
     */
    public boolean isDefaultValueSet() {
        return defaultValueSet;
    }
}
