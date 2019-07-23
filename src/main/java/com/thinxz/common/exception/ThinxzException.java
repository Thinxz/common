package com.thinxz.common.exception;

import lombok.Data;

/**
 * 业务异常定义
 *
 * @author yuan
 */
@Data
public class ThinxzException extends RuntimeException {

    private String errorCode;

    private Object[] params;

    public ThinxzException() {
        super();
    }

    public ThinxzException(String errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public ThinxzException(String errorCode, Object... params) {
        this.errorCode = errorCode;
        this.params = params;
    }

    public ThinxzException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
