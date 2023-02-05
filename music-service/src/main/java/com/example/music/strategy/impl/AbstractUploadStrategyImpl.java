package com.example.music.strategy.impl;

import com.example.music.entity.CustomzieException;
import com.example.music.utils.FileUtils;
import com.example.music.strategy.UploadStrategy;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @description: 规范上传的模板
 * @author: xuyujie
 * @date: 2023/02/04
 **/
public abstract class AbstractUploadStrategyImpl implements UploadStrategy {
    @Override
    public String uploadFile(MultipartFile multipartFile, String filePath) {
        try {
            //获取文件的Md5值
            String md5 = FileUtils.getMd5(multipartFile.getInputStream());
            //获取文件的拓展名
            String extName = FileUtils.getExtName(multipartFile.getOriginalFilename());
            //重新生成文件名
            String fileName = md5 + extName;
            //判断文件是否存在
            if (!exists(filePath + fileName)) {
                upload(filePath, fileName, multipartFile.getInputStream());
            }
            return getFileAccessUrl(filePath + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomzieException("文件上传失败");
        }
    }

    @Override
    public String uploadFile(String fileName, InputStream inputStream, String filePath) {
        try {
            //上传文件
            upload(filePath, fileName, inputStream);
            return getFileAccessUrl(filePath + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomzieException("文件上传失败");
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@link Boolean}
     */
    public abstract Boolean exists(String filePath);

    /**
     * 上传
     *
     * @param path        路径
     * @param fileName    文件名
     * @param inputStream 输入流
     * @throws IOException io异常
     */
    public abstract void upload(String path, String fileName, InputStream inputStream) throws IOException;

    /**
     * 获取文件访问url
     *
     * @param filePath 文件路径
     * @return {@link String}
     */
    public abstract String getFileAccessUrl(String filePath);
}
