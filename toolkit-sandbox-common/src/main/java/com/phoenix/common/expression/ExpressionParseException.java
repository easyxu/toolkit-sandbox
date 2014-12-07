package com.phoenix.common.expression;

import com.phoenix.exceptions.ChainedException;

/**
 * 代表解析表达式的异常。
 *
 * @author Michael Zhou
 * @version $Id: ExpressionParseException.java 1291 2005-03-04 03:23:30Z baobao $
 */
public class ExpressionParseException extends ChainedException {
    private static final long serialVersionUID = 3905519419391424825L;

    /**
     * 创建一个异常。
     */
    public ExpressionParseException() {
        super();
    }

    /**
     * 创建一个异常。
     *
     * @param message 异常信息
     */
    public ExpressionParseException(String message) {
        super(message);
    }

    /**
     * 创建一个异常。
     *
     * @param message 异常信息
     * @param cause 异常原因
     */
    public ExpressionParseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 创建一个异常。
     *
     * @param cause 异常原因
     */
    public ExpressionParseException(Throwable cause) {
        super(cause);
    }
}
