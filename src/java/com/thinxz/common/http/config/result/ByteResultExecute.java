package com.thinxz.common.http.config.result;

import okhttp3.Request;
import okhttp3.Response;
import org.springframework.util.StopWatch;

import java.io.IOException;

public class ByteResultExecute implements ResultExecute<byte[]> {

    @Override
    public byte[] toBody(Response response, Request request, StopWatch stopWatch) throws IOException {
        switch (response.code()) {
            case 200:
                return response.body().bytes();
            default:
                throw HttpException.make(response.code() + " ==> " + response.body().string(), request, stopWatch);
        }
    }
}
