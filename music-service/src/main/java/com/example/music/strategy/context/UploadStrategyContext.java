package com.example.music.strategy.context;

import com.example.music.entity.enums.UploadModeEnum;
import com.example.music.strategy.UploadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;


/**
 * @description: 文件上传上下文
 * @author: xuyujie
 * @date: 2023/02/04
 **/

@Service
public class UploadStrategyContext {

    @Value("${upload.mode}")
    private String mode;

    @Autowired
    public Map<String, UploadStrategy> uploadStrategyMap;

    /**
     * 执行上传的策略
     * @param multipartFile 文件
     * @param path 文件地址
     * @return 上传的地址
     */
    public String executeUploadStrategy(MultipartFile multipartFile, String path) {
        return uploadStrategyMap.get(UploadModeEnum.getStrategy(mode)).uploadFile(multipartFile, path);
    }

    /**
     *
     * @param fileName  文件名
     * @param inputStream   输入流
     * @param filePath  文件路径
     * @return 上传后的地址
     */
    public String executeUploadStrategy(String fileName, InputStream inputStream, String filePath) {
        return uploadStrategyMap.get(UploadModeEnum.getStrategy(mode)).uploadFile(fileName, inputStream, filePath);
    }
}
