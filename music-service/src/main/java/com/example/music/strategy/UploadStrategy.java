package com.example.music.strategy;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 上传文件的策略模式
 */

public interface UploadStrategy {

    /**
     * 上传文件
     * @param multipartFile 文件
     * @param filePath 文件路径，用以区分不同业务下的文件
     * @return
     */
    String uploadFile(MultipartFile multipartFile, String filePath);

    /**
     *
     * @param fileName  文件名
     * @param inputStream   输入流
     * @param filePath  文件地址
     * @return
     */
    String uploadFile(String fileName, InputStream inputStream, String filePath);
}
