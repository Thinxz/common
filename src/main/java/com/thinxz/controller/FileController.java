package com.thinxz.controller;

import com.thinxz.common.exception.ThinxzException;
import com.thinxz.fastdfs.FastDFSClient;
import com.thinxz.fastdfs.config.FileResponse;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
@Api(tags = "FastDFS 文档接口")
@RestController
@RequestMapping("/fastdfs")
public class FileController {

    @Autowired
    private FastDFSClient fastDFSClient;

    @ApiOperation(value = "上传文件")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "request", paramType = "body", dataType = "MultipartFile")
    )
    @RequestMapping(value = "/upload/file", method = RequestMethod.POST)
    @ResponseBody
    public FileResponse uploadFile(@RequestParam MultipartFile file, HttpServletRequest request) {
        return fastDFSClient.upload(file, request, null);
    }

    @ApiOperation(value = "上传图片")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "request", paramType = "body", dataType = "MultipartFile")
    )
    @RequestMapping(value = "/upload/image", method = RequestMethod.POST)
    @ResponseBody
    public FileResponse uploadImage(@RequestParam MultipartFile file, HttpServletRequest request) {
        return fastDFSClient.upload(file, request, "checkImage");
    }

    @ApiOperation(value = "上传文档")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "request", paramType = "body", dataType = "MultipartFile")
    )
    @RequestMapping(value = "/upload/doc", method = RequestMethod.POST)
    @ResponseBody
    public FileResponse uploadDoc(@RequestParam MultipartFile file, HttpServletRequest request) {
        return fastDFSClient.upload(file, request, "checkDoc");
    }

    @ApiOperation(value = "下载文件 [附件形式]")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "request", paramType = "body", dataType = "String")
    )
    @RequestMapping(value = "/download/file", method = {RequestMethod.POST, RequestMethod.GET})
    public void downloadFile(
            @ApiParam(name = "filePath", value = "访问文件全路径", required = true, example = "group1/M00/00/00/rBEABV029uyAETSxAAB0aH2orBg631.jpg")
            @RequestParam(value = "filePath") String filePath,
            HttpServletResponse response) {
        fastDFSClient.downloadFile(filePath, response);
    }

    @ApiOperation(value = "获取图片 [使用输出流输出字节码，可以使用< img> 标签显示图片]")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "request", paramType = "body", dataType = "String")
    )
    @RequestMapping(value = "/download/image", method = {RequestMethod.POST, RequestMethod.GET})
    public void downloadImage(
            @ApiParam(name = "filePath", value = "访问文件全路径", required = true, example = "group1/M00/00/00/rBEABV029uyAETSxAAB0aH2orBg631.jpg")
            @RequestParam(value = "filePath") String filePath,
            HttpServletResponse response) {
        try {
            fastDFSClient.downloadFile(filePath, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "根据指定的路径删除服务器文件，适用于没有保存数据库记录的文件")
    @RequestMapping(value = "/delete/file", method = RequestMethod.DELETE)
    @ResponseBody
    public FileResponse deleteFile(
            @ApiParam(name = "filePath", value = "访问文件全路径", required = true, example = "group1/M00/00/00/rBEABV029uyAETSxAAB0aH2orBg631.jpg")
            @RequestParam(value = "filePath") String filePath,
            @ApiParam(required = false) @RequestParam(value = "locale", required = false) Locale locale) {
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

    @ApiOperation(value = "获取访问文件的 Token")
    @RequestMapping(value = "/get/token", method = {RequestMethod.GET})
    @ResponseBody
    public FileResponse getToken(
            @ApiParam(name = "filePath", value = "访问文件全路径", required = true, example = "group1/M00/00/00/rBEABV029uyAETSxAAB0aH2orBg631.jpg")
            @RequestParam(value = "filePath") String filePath) {
        return fastDFSClient.getToken(filePath);
    }

}
