package com.thinxz.common.http.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * HTTP 配置
 *
 * @author thinxz
 */
@Data
@ConfigurationProperties(prefix = "http")
public class HttpProperty {

    private int httpMaxConnect = 10;

    private long httpKeepAlice = 1;
}
