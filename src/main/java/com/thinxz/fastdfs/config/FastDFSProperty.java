package com.thinxz.fastdfs.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author thinxz
 */
@Data
@ConfigurationProperties(prefix = "fastdfs")
public class FastDFSProperty {

    /**
     * 是否开始密钥访问[防盗链]
     */
    private String httpAntiStealToken = "true";

    /**
     * 密钥
     */
    private String httpSecretKey = "FastDFS1234567890";

    private String httpTrackerHttpPort = "80";

    /**
     * tracker 服务器组
     */
    private String trackerServers = "thinxz.cn:22122";

    /**
     * 文件服务器地址
     */
    private String fileServer = "thinxz.cn:22122";

}
