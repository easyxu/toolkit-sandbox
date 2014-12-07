package com.phoenix.common.property.parser;

import com.phoenix.exceptions.ChainedException;

/**
 * Parser的异常类.
 *
 * @author <a href="mailto:zyh@alibaba-inc.com">Michael Zhou</a>
 * @version $Id: ParseException.java 1291 2005-03-04 03:23:30Z baobao $
 */
public class ParseException extends ChainedException {
    

    /**
	 * 
	 */
	private static final long serialVersionUID = 2928013060227784980L;

	/**
     * 构造一个异常, 指明异常的详细信息.
     *
     * @param message 详细信息.
     */
    public ParseException(String message) {
        super(message);
    }

    /**
     * 构造一个异常, 指明引起这个异常的异常或错误.
     *
     * @param cause 引起这个异常的异常或错误.
     */
    public ParseException(Throwable cause) {
        super(cause);
    }
}
