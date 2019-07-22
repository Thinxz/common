package com.thinxz.common.http.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.MediaType;

import java.io.InputStream;

/**
 * HTTP 参数
 *
 * @author thinxz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HttpMultiParam {

    private String fileName;

    private InputStream inputStream;

    private MediaType mediaType;
}
