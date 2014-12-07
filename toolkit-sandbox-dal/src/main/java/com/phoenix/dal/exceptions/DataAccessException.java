package com.phoenix.dal.exceptions;


import com.phoenix.exceptions.ChainedException;
import com.phoenix.exceptions.ErrorCode;

/**
 * Created by xux on 14-12-7.
 */
public class DataAccessException extends ChainedException {

    public DataAccessException() {
    }

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(Throwable cause) {
        super(cause);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
