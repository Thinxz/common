package com.thinxz.controller;

import com.thinxz.common.exception.ThinxzException;
import com.thinxz.fastdfs.FastDFSClient;
import com.thinxz.fastdfs.config.FileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * FASTDFS 接口
 *
 * @author thinxz
 */
@Controller
@RequestMapping("/fastdfs")
public class FileController {

    @Autowired
    private FastDFSClient fastDFSClient;

    /**
     * 上传服务器文件
     *
     * @param fileName
     * @return
     */
    @RequestMapping(value = "/upload/file/sample")
    @ResponseBody
    public FileResponse uploadFile(String fileName) {
        return new FileResponse("success", fastDFSClient.upload("D:\\thinxz\\config\\temp3.jpg", null));
    }

    /**
     * 文件通用上传
     *
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload/file")
    @ResponseBody
    public FileResponse uploadFile(MultipartFile file, HttpServletRequest request) {
        return fastDFSClient.upload(file, request, null);
    }

    /**
     * 上传图片
     *
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("/upload/image")
    @ResponseBody
    public FileResponse uploadImage(@RequestParam MultipartFile file, HttpServletRequest request) {
        return fastDFSClient.upload(file, request, "checkImage");
    }

    /**
     * 上传文档
     *
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("/upload/doc")
    @ResponseBody
    public FileResponse uploadDoc(@RequestParam MultipartFile file, HttpServletRequest request) {
        return fastDFSClient.upload(file, request, "checkDoc");
    }

    /**
     * 下载文件[附件形式]
     *
     * @param filePath
     * @param response
     */
    @RequestMapping("/download/file")
    public void downloadFile(String filePath, HttpServletResponse response) {
        fastDFSClient.downloadFile(filePath, response);
    }

    /**
     * 获取图片 使用输出流输出字节码，可以使用< img> 标签显示图片
     *
     * @param filePath
     * @param response
     */
    @RequestMapping("/download/image")
    public void downloadImage(String filePath, HttpServletResponse response) {
        try {
            fastDFSClient.downloadFile(filePath, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据指定的路径删除服务器文件，适用于没有保存数据库记录的文件
     *
     * @param filePath
     * @param locale
     * @return
     */
    @RequestMapping("/delete/file")
    public FileResponse deleteFile(String filePath, Locale locale) {
        FileResponse responseData = new FileResponse();
        try {
            fastDFSClient.deleteFile(filePath);
        } catch (ThinxzException e) {
            e.printStackTrace();
            responseData.setStatus("error");
            responseData.setCode(e.getErrorCode());
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     * 获取访问文件的 Token
     *
     * @param filePath
     * @return
     */
    @RequestMapping("/get/token")
    @ResponseBody
    public FileResponse getToken(String filePath) {
        filePath = "group1/M00/00/00/fwAAAV014DqAW7cYAAEEaOWOhlc580.jpg";
        return fastDFSClient.getToken(filePath);
    }

}
