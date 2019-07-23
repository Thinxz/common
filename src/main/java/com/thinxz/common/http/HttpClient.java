package com.thinxz.common.http;

import com.thinxz.common.http.config.result.HttpException;
import com.thinxz.common.http.config.HttpParams;
import com.thinxz.common.http.config.result.ResultExecute;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * HTTP 客户端
 *
 * @author thinxz
 */
@Component
public class HttpClient {

    @Autowired
    private OkHttpClient client;

    public <T> T execute(HttpParams params, ResultExecute<T> resultExecute) {
        Request request = params.toRequest();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Response response = null;
        try {
            // 请求
            response = client.newCall(request).execute();
            // 监控
            stopWatch.stop();
            // 返回解析结果
            return resultExecute.toBody(response, request, stopWatch);
        } catch (HttpException e) {
            throw e;
        } catch (Exception e) {
            throw HttpException.make(e, request, stopWatch);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public Response execute(HttpParams params) {
        Request request = params.toRequest();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Response response = null;
        try {
            // 请求
            response = client.newCall(request).execute();
            // 监控
            stopWatch.stop();
            // 响应结果
            return response;
        } catch (HttpException e) {
            throw e;
        } catch (Exception e) {
            throw HttpException.make(e, request, stopWatch);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
