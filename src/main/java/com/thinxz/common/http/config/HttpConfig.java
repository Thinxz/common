package com.thinxz.common.http.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * HTTP 配置
 *
 * @author thinxz
 */
@Configuration
@EnableConfigurationProperties(HttpProperty.class)
public class HttpConfig {

    @Autowired
    private HttpProperty httpProperty;

    @Bean
    OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .followRedirects(false)
                .readTimeout(1, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false)
                .writeTimeout(1, TimeUnit.MINUTES)
                .connectionPool(new ConnectionPool(httpProperty.getHttpMaxConnect(), httpProperty.getHttpKeepAlice(), TimeUnit.MINUTES))
                .build();
    }

}
