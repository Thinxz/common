package com.thinxz.fastdfs;

import com.thinxz.common.exception.ThinxzException;
import com.thinxz.fastdfs.config.TrackerServerPool;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FastDFS -  客户端访问
 *
 * @author thinxz
 */
public class FastDFS {

    @Autowired
    private TrackerServerPool trackerServerPool;

    /**
     * @param dataBytes 数据缓存
     * @param fileName  文件名
     * @param desc      文件描述
     * @return 组名 + 文件路径
     */
    public String upload(byte[] dataBytes, String fileName, Map<String, String> desc) {
        TrackerServer trackerServer = null;
        String path = null;
        try {
            // 文件描述
            NameValuePair[] nvps = null;
            String suffix = null;
            try {
                suffix = FileUtil.getFilenameSuffix(fileName);

                if (Strings.isBlank(fileName) || Strings.isBlank(suffix)) {
                    System.out.println(String.format("文件名或后缀不能未空"));
                    return null;
                }

                if (desc == null) {
                    // 文件名
                    desc = new HashMap<>();
                }

                // 文件后缀, 文件格式
                fileName = FileUtil.toLocal(fileName);
                desc.put("filename", fileName);

                List<NameValuePair> nvpsList = new ArrayList<>();
                desc.forEach((k, v) -> nvpsList.add(new NameValuePair(k, v)));
                nvps = new NameValuePair[desc.size()];
                nvpsList.toArray(nvps);
            } catch (Exception e) {
                System.out.println(String.format("文件描述参数解析错误 => ", desc));
            }

            trackerServer = trackerServerPool.borrowObject();
            StorageClient1 storageClient = new StorageClient1(trackerServer, null);

            // 上传文件
            path = storageClient.upload_file1(dataBytes, suffix, nvps);

            if (Strings.isBlank(path)) {
                throw new ThinxzException("100003", "文件上传失败 -> PATH 返回空");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ThinxzException("100003", "文件上传失败, IO 错误", e);
        } catch (MyException e) {
            e.printStackTrace();
            throw new ThinxzException("100003", "文件上传失败", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ThinxzException("100003", "文件上传失败", e);
        } finally {
            if (trackerServer != null) {
                // 释放资源
                trackerServerPool.returnObject(trackerServer);
            }
        }

        return path;
    }

    /**
     * 下载文件 [http://localhost:8081/thinxz-common/fastdfs/download/image?filePath=group1/M00/00/00/rBEABV029uyAETSxAAB0aH2orBg631.jpg]
     *
     * @param filepath 文件路径
     * @param filename 文件名称
     * @param os       输出流
     * @param response HttpServletResponse
     */
    public void download(String filepath, String filename, OutputStream os, HttpServletResponse response) {
        if (StringUtils.isBlank(filepath)) {
            throw new ThinxzException("100001", "文件路径为空");
        }

        // 转换路径, 并去除空白符等
        filepath = FileUtil.toLocal(filepath).replace("\"", "").trim();
        // 文件名
        if (StringUtils.isBlank(filename)) {
            // 整理路径
            filepath = FileUtil.toLocal(filepath);
            // 获取文件名
            filename = filepath.substring(filepath.lastIndexOf("/") + 1);
        }

        InputStream is = null;
        TrackerServer trackerServer = null;
        try {
            // 获取对象
            trackerServer = trackerServerPool.borrowObject();
            StorageClient1 storageClient = new StorageClient1(trackerServer, null);
            byte[] fileByte = storageClient.download_file1(filepath);
            // 将数据写入输出流
            // IOUtils.write(fileByte, new FileOutputStream("C:\\Users\\thinx\\Desktop\\2.txt"));

            if (fileByte == null) {
                throw new ThinxzException("100004", "文件不存在");
            }

            if (response != null) {
                os = response.getOutputStream();

                // 设置响应头
                String contentType = FileUtil.contentType(FileUtil.getFilenameSuffix(filename));
                if (StringUtils.isNotBlank(contentType)) {
                    // 文件编码 处理文件名中的 '+'、' ' 特殊字符
                    String encoderName = URLEncoder.encode(filename, "UTF-8").replace("+", "%20").replace("%2B", "+");
                    response.setHeader("Content-Disposition", "attachment;filename=\"" + encoderName + "\"");
                    response.setContentType(contentType + ";charset=UTF-8");
                    response.setHeader("Accept-Ranges", "bytes");
                }
            }

            is = new ByteArrayInputStream(fileByte);
            byte[] buffer = new byte[1024 * 5];
            int len = 0;
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
            throw new ThinxzException("100005", "文件下载失败");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }

                if (trackerServer != null) {
                    // 释放资源
                    trackerServerPool.returnObject(trackerServer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载文件
     *
     * @param filepath 文件路径
     * @return 返回文件字节
     */
    public byte[] download(String filepath) {
        if (StringUtils.isBlank(filepath)) {
            throw new ThinxzException("100001", "文件路径为空");
        }

        byte[] fileByte = null;
        TrackerServer trackerServer = null;
        try {
            // 获取对象
            trackerServer = trackerServerPool.borrowObject();
            StorageClient1 storageClient = new StorageClient1(trackerServer, null);
            fileByte = storageClient.download_file1(filepath);
            if (fileByte == null) {
                throw new ThinxzException("100004", "文件不存在");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            throw new ThinxzException("100005", "文件下载失败");
        } catch (Exception e) {
            if (trackerServer != null) {
                // 释放资源
                trackerServerPool.returnObject(trackerServer);
            }
        }

        return fileByte;
    }

    /**
     * 删除文件
     *
     * @param filepath 文件路径
     * @return 删除成功返回 0, 失败返回其它
     */
    public int deleteFile(String filepath) {
        if (StringUtils.isBlank(filepath)) {
            throw new ThinxzException("100001", "文件路径为空");
        }

        TrackerServer trackerServer = null;
        int success = 0;
        try {
            StorageClient1 storageClient = new StorageClient1(trackerServer, null);

            success = storageClient.delete_file1(filepath);
            if (success != 0) {
                throw new ThinxzException("100006", "删除文件失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            throw new ThinxzException("100006", "删除文件失败");
        } catch (Exception e) {
            if (trackerServer != null) {
                // 释放资源
                trackerServerPool.returnObject(trackerServer);
            }
        }

        return success;
    }

    /**
     * 获取文件信息
     *
     * @param filepath 文件路径
     * @return 文件信息
     *
     * <pre>
     *  {<br>
     *      "SourceIpAddr": 源IP <br>
     *      "FileSize": 文件大小 <br>
     *      "CreateTime": 创建时间 <br>
     *      "CRC32": 签名 <br>
     *  }  <br>
     * </pre>
     */
    public Map<String, Object> getFileInfo(String filepath) {
        TrackerServer trackerServer = null;
        Map<String, Object> infoMap = null;
        try {
            // 获取对象
            trackerServer = trackerServerPool.borrowObject();
            StorageClient1 storageClient = new StorageClient1(trackerServer, null);
            FileInfo fileInfo = storageClient.get_file_info1(filepath);

            // 解析
            infoMap = new HashMap<>(4);
            infoMap.put("SourceIpAddr", fileInfo.getSourceIpAddr());
            infoMap.put("FileSize", fileInfo.getFileSize());
            infoMap.put("CreateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fileInfo.getCreateTimestamp()));
            infoMap.put("CRC32", fileInfo.getCrc32());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (trackerServer != null) {
                // 释放资源
                trackerServerPool.returnObject(trackerServer);
            }
        }
        return infoMap;
    }

    /**
     * 获取文件描述信息
     *
     * @param filepath 文件路径
     * @return 文件描述信息
     */
    public Map<String, Object> getFileDescriptions(String filepath) {
        TrackerServer trackerServer = null;
        try {
            // 获取对象
            trackerServer = trackerServerPool.borrowObject();
            StorageClient1 storageClient = new StorageClient1(trackerServer, null);
            // 查询
            NameValuePair[] nvps = storageClient.get_metadata1(filepath);
            // 解析描述
            Map<String, Object> infoMap = null;
            if (nvps != null && nvps.length > 0) {
                infoMap = new HashMap<>(nvps.length);
                for (NameValuePair nvp : nvps) {
                    infoMap.put(nvp.getName(), nvp.getValue());
                }
            }
            return infoMap;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (trackerServer != null) {
                // 释放资源
                trackerServerPool.returnObject(trackerServer);
            }
        }
        return null;
    }

}
