package com.thinxz.common.http.config.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import okhttp3.Request;
import org.springframework.util.StopWatch;

/**
 * HTTP 请求异常
 *
 * @author thinxz
 */
@Data
@AllArgsConstructor
public class HttpException extends RuntimeException {

    private Request request;

    private StopWatch stopWatch;

    public HttpException() {
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(Throwable cause) {
        super(cause);
    }

    public HttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static HttpException make(String msg, Request req, StopWatch s) {
        HttpException exception = new HttpException(msg);
        exception.setRequest(req);
        exception.setStopWatch(s);
        return exception;
    }

    public static HttpException make(Exception e, Request req, StopWatch s) {
        HttpException exception = new HttpException(e);
        exception.setRequest(req);
        exception.setStopWatch(s);
        return exception;
    }

    @Override
    public HttpException fillInStackTrace() {
        return this;
    }

}
