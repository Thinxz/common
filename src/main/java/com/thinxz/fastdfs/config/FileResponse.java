package com.thinxz.fastdfs.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author thinxz
 */
@Data
@NoArgsConstructor
public class FileResponse {

    public FileResponse(String status) {
        this.status = status;
    }

    public FileResponse(String status, String filePath) {
        this.status = status;
        this.filePath = filePath;
    }

    /**
     * 返回状态编码
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;

    /**
     * 返回信息
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    /**
     * 成功标识
     */
    private String status = "success";

    /**
     * 文件路径
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String filePath;

    /**
     * 文件名称
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fileName;

    /**
     * 文件类型
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fileType;

    /**
     * Http URL
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String httpUrl;

    /**
     * Http Token
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    /**
     * 时间戳
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ts;

}
