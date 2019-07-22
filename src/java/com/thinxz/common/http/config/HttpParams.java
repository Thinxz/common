package com.thinxz.common.http.config;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.*;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * HTTP 参数
 *
 * @author thinxz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HttpParams {

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_STRING = MediaType.parse("text/plain");
    public static final MediaType MEDIA_TYPE_FILE = MediaType.parse("application/oct-stream");

    private String method;

    private String url;

    private boolean string;

    private String stringBody;

    private Map<String, Object> params;

    private boolean json;

    private boolean file;

    private String userAgent;

    private Map<String, String> heads;

    public HttpParams put(String name, Object value) {
        if (params == null) {
            params = new TreeMap<>();
        }
        params.put(name, value);
        return this;
    }

    public Request toRequest() {
        Request.Builder request;
        switch (method.toUpperCase()) {
            case "GET":
                request = this.toGet();
                break;
            case "POST":
                request = this.toPost();
                break;
            default:
                throw new RuntimeException("not support => " + method);
        }
        if (StringUtils.isNotBlank(userAgent)) {
            request.addHeader("User-Agent", userAgent);
        }
        if (heads != null) {
            heads.forEach((k, v) -> {
                request.addHeader(k, v);
            });
        }
        return request.build();
    }

    private Request.Builder toGet() {
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            params.forEach((k, v) -> {
                if (v == null) {
                    return;
                }
                builder.addEncodedQueryParameter(k, v.toString());
            });
        }
        return new Request.Builder().url(builder.build());
    }

    private Request.Builder toPost() {
        // string
        if (string) {
            RequestBody body = RequestBody.create(MEDIA_TYPE_STRING, stringBody);
            return new Request.Builder().url(url).post(body);
        }
        // json提交
        if (json) {
            String jsonString = JSON.toJSONString(params);
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, jsonString);
            return new Request.Builder().url(url).post(body);
        }
        // 上传文件
        else if (file) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            params.forEach((k, v) -> {
                if (v == null) {
                    return;
                }
                if (v instanceof File) {
                    File f = (File) v;
                    builder.addFormDataPart(
                            k, f.getName(), RequestBody.create(MEDIA_TYPE_FILE, f)
                    );
                } else if (v instanceof InputStream) {
                    builder.addFormDataPart(
                            k, k, create(MEDIA_TYPE_FILE, (InputStream) v)
                    );
                } else if (v instanceof HttpMultiParam) {
                    HttpMultiParam p = (HttpMultiParam) v;
                    builder.addFormDataPart(
                            k, p.getFileName(), create(p.getMediaType(), p.getInputStream())
                    );
                }
                //
                else {
                    builder.addFormDataPart(k, v.toString());
                }
            });
            return new Request.Builder().url(url).post(builder.build());
        }
        // 上传
        else {
            FormBody.Builder builder = new FormBody.Builder();
            if (params != null) {
                params.forEach((k, v) -> {
                    if (v == null) {
                        return;
                    }
                    builder.addEncoded(k, v.toString());
                });
            }
            return new Request.Builder().url(url).post(builder.build());
        }
    }

    public static RequestBody create(final MediaType mediaType, final InputStream inputStream) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() {
                try {
                    return inputStream.available();
                } catch (IOException e) {
                    return 0;
                }
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    sink.writeAll(source);
                } finally {
                }
            }
        };
    }

    public static HttpParams get(String url) {
        return HttpParams
                .builder()
                .url(url)
                .method("GET")
                .params(new TreeMap<>())
                .build();
    }

    public static HttpParams get(String url, Map<String, Object> params) {
        return HttpParams
                .builder()
                .url(url)
                .params(params)
                .method("GET")
                .build();
    }

    public static HttpParams post(String url, Map<String, Object> params) {
        return HttpParams
                .builder()
                .url(url)
                .params(params)
                .method("POST")
                .build();
    }

    public static HttpParams postFile(String url, Map<String, Object> params) {
        return HttpParams
                .builder()
                .url(url)
                .params(params)
                .method("POST")
                .file(true)
                .build();
    }

    public static HttpParams postString(String url, String body) {
        return HttpParams
                .builder()
                .url(url)
                .string(true)
                .stringBody(body)
                .method("POST")
                .build();
    }

    public static HttpParams postJson(String url, Map<String, Object> params) {
        return HttpParams
                .builder()
                .url(url)
                .params(params)
                .method("POST")
                .json(true)
                .build();
    }

    public HttpParams putHead(String k, String v) {
        if (heads == null) {
            heads = new TreeMap<>();
        }
        heads.put(k, v);
        return this;
    }

}


