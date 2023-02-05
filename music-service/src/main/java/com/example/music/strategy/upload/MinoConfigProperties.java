package com.example.music.strategy.upload;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "upload.mino")
public class MinoConfigProperties {
    private String secretId;
    private String secretKey;
    private String gateway;
    private String bucketName;
}
