package com.thinxz.common.http.config.result;

import okhttp3.Request;
import okhttp3.Response;
import org.springframework.util.StopWatch;

import java.io.IOException;

public class StringResultExecute implements ResultExecute<String> {

    @Override
    public String toBody(Response response, Request request, StopWatch stopWatch) throws IOException {
        switch (response.code()) {
            case 200:
                return response.body().string();
            default:
                throw HttpException.make(response.code() + " ==> " + response.body().string(), request, stopWatch);
        }
    }
}
