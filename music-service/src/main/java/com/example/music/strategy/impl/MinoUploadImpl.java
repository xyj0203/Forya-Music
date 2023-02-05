package com.example.music.strategy.impl;

import com.example.music.strategy.impl.AbstractUploadStrategyImpl;
import com.example.music.strategy.upload.MinoConfigProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service("minoUploadStrategyImpl")
@Slf4j
public class MinoUploadImpl extends AbstractUploadStrategyImpl {
    private MinioClient minioClient;
    private final MinoConfigProperties minoConfigProperties;
    public MinoUploadImpl(MinoConfigProperties minoConfigProperties) {
        this.minoConfigProperties = minoConfigProperties;
        createMinoClient();
    }

    @Override
    public Boolean exists(String filePath) {
        boolean exist = true;
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(minoConfigProperties.getBucketName()).object(filePath).build());
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) throws IOException {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minoConfigProperties.getBucketName())
                            .object(path+fileName)
                            .contentType("image/png")
                            .stream(inputStream, inputStream.available(), -1)
                            .build());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        StringBuilder stringBuilder = new StringBuilder(minoConfigProperties.getGateway());
        stringBuilder.append(minoConfigProperties.getBucketName());
        stringBuilder.append("/");
        stringBuilder.append(filePath);
        return stringBuilder.toString();
    }

    public void createMinoClient() {
        try {
            if (null == minioClient) {
                log.info("开始创建 MinioClient...");
                minioClient = MinioClient
                        .builder()
                        .endpoint(minoConfigProperties.getGateway())
                        .credentials(minoConfigProperties.getSecretId(), minoConfigProperties.getSecretKey())
                        .build();
                log.info("创建完毕 MinioClient...");
            }
        } catch (Exception e) {
            log.error("MinIO服务器异常：{}", e);
        }
    }
}
