package com.thinxz.fastdfs;

import com.thinxz.common.exception.ThinxzException;
import com.thinxz.fastdfs.config.FileResponse;
import org.csource.common.MyException;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StorageClient1;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Map;

/**
 * 业务校验并调用接口上传下载文件
 *
 * @author thinxz
 */
@Service
public class FastDFSClient extends FastDFS {

    /**
     * 上传指定路径文件
     *
     * @param filepath
     * @param descriptions
     */
    public String upload(String filepath, Map<String, String> descriptions) {
        File file = new File(filepath);
        String path = null;
        try {
            InputStream is = new FileInputStream(file);

            // 获取文件名
            filepath = FileUtil.toLocal(filepath);
            // 获取文件名
            String filename = filepath.substring(filepath.lastIndexOf("/") + 1);

            // 文件
            path = upload(is, filename, descriptions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 上传通用方法，只上传到服务器，不保存记录到数据库
     */
    public FileResponse upload(MultipartFile file, HttpServletRequest request, String check) {
        FileResponse responseData;
        if ("checkImage".equalsIgnoreCase(check)) {
            // 图片类型校验类型
            // 检查文件格式是否正确，默认只能上传 ['png', 'gif', 'jpeg', 'jpg'] 几种类型.
            if (!FileUtil.checkImage(file.getOriginalFilename())) {
                responseData = new FileResponse("");
                responseData.setCode("FILE_TYPE_ERROR_IMAGE");
                responseData.setMessage("FILE_TYPE_ERROR_IMAGE");
                return responseData;
            }
        } else if ("checkDoc".equalsIgnoreCase(check)) {
            // 文档类型校验
            // 检查文件格式是否正确，默认只能上传 ['pdf', 'ppt', 'xls', 'xlsx', 'pptx', 'doc', 'docx'] 几种类型.
            if (!FileUtil.checkDoc(file.getOriginalFilename())) {
                responseData = new FileResponse("error");
                responseData.setCode("FILE_TYPE_ERROR_DOC");
                responseData.setMessage("FILE_TYPE_ERROR_DOC");
                return responseData;
            }
        }

        try {
            // 上传到服务器
            String filepath = uploadFileWithMultipart(file);
            responseData = getToken(filepath);
            responseData.setFileName(file.getOriginalFilename());
            responseData.setFilePath(filepath);
            responseData.setFileType(FileUtil.getFilenameSuffix(file.getOriginalFilename()));
        } catch (ThinxzException e) {
            responseData = new FileResponse("error");
            responseData.setCode(e.getErrorCode());
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     * 文件输入流
     */
    public String upload(InputStream is, String filename, Map<String, String> descriptions) {
        try {
            if (is == null) {
                throw new ThinxzException("100002", "文件为空");
            }

            if (is.available() > 100 * 1000 * 1000) {
                throw new ThinxzException("100008", "文件超过大小");
            }

            // 读取流
            byte[] fileBuff = new byte[is.available()];
            // 读取文件流
            is.read(fileBuff, 0, fileBuff.length);
            //
            return this.upload(fileBuff, filename, descriptions);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * MultipartFile 上传文件
     *
     * @param file MultipartFile
     * @return 返回上传成功后的文件路径
     */
    public String uploadFileWithMultipart(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ThinxzException("100002", "文件为空");
        }

        try {
            return upload(file.getInputStream(), file.getOriginalFilename(), null);
        } catch (IOException e) {
            throw new ThinxzException("100002", "文件为空");
        }
    }

//
//    /**
//     * MultipartFile 上传文件
//     *
//     * @param file         MultipartFile
//     * @param descriptions 文件描述
//     * @return 返回上传成功后的文件路径
//     */
//    public String uploadFileWithMultipart(MultipartFile file, Map<String, String> descriptions) {
//        return upload(file, descriptions);
//    }
//
//    /**
//     * 根据指定的路径上传文件
//     *
//     * @param filepath 文件路径
//     * @return 返回上传成功后的文件路径
//     */
//    public String uploadFileWithFilepath(String filepath) {
//        return upload(filepath, null);
//    }
//
//    /**
//     * 根据指定的路径上传文件
//     *
//     * @param filepath     文件路径
//     * @param descriptions 文件描述
//     * @return 返回上传成功后的文件路径
//     */
//    public String uploadFileWithFilepath(String filepath, Map<String, String> descriptions) {
//        return upload(filepath, descriptions);
//    }
//
//    /**
//     * 上传base64文件
//     *
//     * @param base64 文件base64
//     * @return 返回上传成功后的文件路径
//     */
//    public String uploadFileWithBase64(String base64) {
//        return upload(base64, null, null);
//    }
//
//    /**
//     * 上传base64文件
//     *
//     * @param base64   文件base64
//     * @param filename 文件名
//     * @return 返回上传成功后的文件路径
//     */
//    public String uploadFileWithBase64(String base64, String filename) {
//        return upload(base64, filename, null);
//    }
//
//    /**
//     * 上传base64文件
//     *
//     * @param base64       文件base64
//     * @param filename     文件名
//     * @param descriptions 文件描述信息
//     * @return 返回上传成功后的文件路径
//     */
//    public String uploadFileWithBase64(String base64, String filename, Map<String, String> descriptions) {
//        return upload(base64, filename, descriptions);
//    }
//
//    /**
//     * 上传Base64文件
//     *
//     * @param base64
//     * @param filename     文件名
//     * @param descriptions 文件描述信息
//     * @return 文件路径
//     */
//    public String upload(String base64, String filename, Map<String, String> descriptions) {
//        if (StringUtils.isBlank(base64)) {
//            throw new ThinxzException("100002", "文件为空");
//        }
//
//        return upload(new ByteArrayInputStream(Base64.decodeBase64(base64)), filename, descriptions);
//    }

    /********* ********** ********* ********* ********** **********/

    /**
     * 以附件形式下载文件
     *
     * @param filepath 文件路径
     * @param response
     */
    public void downloadFile(String filepath, HttpServletResponse response) {
        this.download(filepath, null, null, response);
    }

    /**
     * 以附件形式下载文件 , 指定下载文件名称
     *
     * @param filepath 文件路径
     * @param filename 文件名称
     * @param response HttpServletResponse
     */
    public void downloadFile(String filepath, String filename, HttpServletResponse response) {
        this.download(filepath, filename, null, response);
    }

    /**
     * 下载文件, 只写入输出流
     *
     * @param filepath 文件路径
     * @param os       输出流
     */
    public void downloadFile(String filepath, OutputStream os) {
        this.download(filepath, null, os, null);
    }

    /********* ********** ********* ********* ********** **********/

    /**
     * 获取源文件的文件名称
     *
     * @param filepath 文件路径
     * @return 文件名称
     */
    public String getOriginalFilename(String filepath) {
        Map<String, Object> descriptions = this.getFileDescriptions(filepath);
        if (descriptions.get("filename") != null) {
            return (String) descriptions.get("filename");
        }
        return null;
    }

    /**
     * 获取FastDFS文件的名称
     *
     * @param fileId 包含组名和文件名
     */
    public static String getFilename(String fileId) {
        String[] results = new String[2];
        StorageClient1.split_file_id(fileId, results);
        return results[1];
    }

    /**
     * 获取访问服务器TOKEN，拼接到地址后面 [根据密钥算法生成]
     *
     * @param filepath 文件路径
     * @return 返回token [token=078d370098b03e9020b82c829c205e1f&ts=1508141521]
     */
    public FileResponse getToken(String filepath) {
        FileResponse responseData = new FileResponse();
        // 设置访文件的Http地址. 有时效性.
        // unix seconds
        int ts = (int) Instant.now().getEpochSecond();
        // token
        String token = "null";
        try {
            token = ProtoCommon.getToken(getFilename(filepath), ts, fastDFSProperty.getHttpSecretKey());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

        // 设置访文件的Http地址. 有时效性.
        responseData.setToken(token);
        responseData.setTs(String.format("%s", ts));
        responseData.setHttpUrl(String.format("%s/%s?token=%s&ts=%s", fastDFSProperty.getFileServer(), filepath, responseData.getToken(), responseData.getTs()));
        return responseData;
    }

}
