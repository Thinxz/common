package com.thinxz.fastdfs;

import com.thinxz.fastdfs.config.FileType;
import org.apache.commons.lang3.StringUtils;

/**
 * @author thinxz
 */
public class FileUtil {

    /**
     * 根据文档类型返回响应头
     *
     * @param filenameSuffix
     * @return
     */
    public static String contentType(String filenameSuffix) {
        return FileType.checkValue(filenameSuffix);
    }

    /**
     * 检查图片类型
     *
     * @param filename 可自行传入文件的类型，默认检查 [png, gif, jpeg, jpg]
     * @return
     */
    public static boolean checkImage(String filename) {
        String fileType = FileType.checkValue(getFilenameSuffix(filename));

        return fileType == null || "".equalsIgnoreCase(fileType);
    }

    /**
     * 检查文档类型
     *
     * @param filename 可自行传入文件的类型，默认检查 [pdf, ppt, xls, xlsx, pptx, doc, docx]
     */
    public static boolean checkDoc(String filename) {
        String fileType = FileType.checkValue(getFilenameSuffix(filename));

        return fileType == null || "".equalsIgnoreCase(fileType);
    }

    /**
     * 转换路径中的 '\' 为 '/' <br>
     * 并把文件后缀转为小写
     *
     * @param path 路径
     * @return
     */
    public static String toLocal(String path) {
        if (StringUtils.isNotBlank(path)) {
            path = path.replaceAll("\\\\", "/");
            if (path.contains(".")) {
                String pre = path.substring(0, path.lastIndexOf(".") + 1);
                String suffix = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
                path = pre + suffix;
            }
        }
        return path;
    }

    /**
     * 获取文件名称的后缀
     *
     * @param filename 文件名 或 文件路径
     * @return 文件后缀
     */
    public static String getFilenameSuffix(String filename) {
        String suffix = null;
        if (StringUtils.isNotBlank(filename)) {
            if (filename.contains("/")) {
                filename = filename.substring(filename.lastIndexOf("/") + 1);
            }

            if (filename.contains(".")) {
                suffix = filename.substring(filename.lastIndexOf(".") + 1);
            } else {

            }
        }
        return suffix;
    }

}
