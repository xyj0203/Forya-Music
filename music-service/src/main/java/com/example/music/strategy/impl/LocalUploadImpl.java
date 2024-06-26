package com.example.music.strategy.impl;

import com.example.music.entity.CustomzieException;
import com.example.music.entity.enums.FileExtEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;

/**
 * @description:
 * @author: xuyujie
 * @date: 2023/02/04
 **/
@Service("localUploadStrategyImpl")
public class LocalUploadImpl extends AbstractUploadStrategyImpl{

    /**
     * 本地路径
     */
    @Value("${upload.local.path}")
    private String localPath;

    /**
     * 文件上传的访问Url
     */
    @Value("${upload.local.url}")
    private String localUrl;


    @Override
    public Boolean exists(String filePath) {
        return new File(localPath + filePath).exists();
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) throws IOException {
        //判断目录是否存在
        File directory = new File(localPath + path);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new CustomzieException("创建目录失败！");
            }
        }
        File file = new File(localPath + path + fileName);
        String ext = "." + fileName.split("\\.")[1];
        switch (Objects.requireNonNull(FileExtEnum.getFileExt(ext))) {
            case MD:
            case TXT:
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                while (reader.ready()) {
                    writer.write((char) reader.read());
                }
                writer.flush();
                writer.close();
                reader.close();
            default:
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
                byte[] bytes = new byte[1024];
                int length;
                while ((length = bufferedInputStream.read()) != -1) {
                    bufferedOutputStream.write(bytes,0,length);
                }
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                bufferedInputStream.close();
        }
        inputStream.close();
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return localPath + filePath;
    }
}
