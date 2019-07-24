package com.thinxz.fastdfs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FastDFSProperty.class)
public class FastDFSConfig {

    @Autowired
    private FastDFSProperty fastDFSProperty;

    @Bean
    TrackerServerPool trackerServerPool() {
        return new TrackerServerPool(fastDFSProperty);
    }

}
