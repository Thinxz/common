package com.thinxz.fastdfs.config;

import com.thinxz.common.exception.ThinxzException;

/**
 *
 */
public enum FileType {

    // image
    png("image/png"),
    gif("image/gif"),
    bmp("image/bmp"),
    ico("image/x-ico"),
    jpeg("image/jpeg"),
    jpg("image/jpeg"),
    // 压缩文件 [zip rar]
    zip("application/zip"),
    rar("application/x-rar"),
    // doc [pdf ppt xls xlsx pptx doc docx]
    pdf("application/pdf"),
    ppt("application/vnd.ms-powerpoint"),
    xls("application/vnd.ms-excel"),
    xlsx("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    pptx("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    doc("application/msword"),
    doc_wps("application/wps-office.doc"),
    docx("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    txt("text/plain"),
    // 音频 [mp3 mp4 flv]
    mp4("video/mp4"),
    flv("video/x-flv");

    private String value;

    FileType(String value) {
        this.value = value;
    }

    /**
     * ContentType 检测
     */
    public static String checkValue(String contentType) {
        try {
            return FileType.valueOf(contentType).value;
        } catch (IllegalArgumentException e) {
            throw new ThinxzException("100010", "文件类型定义错误");
        }
    }

}
