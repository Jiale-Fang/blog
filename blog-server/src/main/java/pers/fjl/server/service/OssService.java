package pers.fjl.server.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {

    /**
     * 上传文件
     * @param file 文件流
     * @param folderName 文件夹名
     * @return
     */
    String uploadImg(MultipartFile file, String folderName);
}
