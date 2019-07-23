package com.thinxz.common.http.config.result;

import okhttp3.Request;
import okhttp3.Response;
import org.springframework.util.StopWatch;

/**
 * 返回结果解析
 *
 * @author thinxz
 */
public interface ResultExecute<T> {

    /**
     * 解析HTTP 请求结果
     *
     * @param response
     * @param request
     * @param stopWatch
     * @return
     * @throws Exception
     */
    T toBody(Response response, Request request, StopWatch stopWatch) throws Exception;
}
