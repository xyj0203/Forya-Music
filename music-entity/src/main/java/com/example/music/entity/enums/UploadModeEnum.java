package com.example.music.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:    上传模式的枚举
 * @author: xuyujie
 * @date: 2023/02/04
 **/
@Getter
@AllArgsConstructor
public enum UploadModeEnum {

    /**
     * OSS
     */
    OSS("oss","ossUploadStrategyImpl"),

    /**
     * 本地
     */
    LOCAL("local", "localUploadStrategyImpl"),

    /**
     * mino
     */
    MINO("mino", "minoUploadStrategyImpl");

    /**
     * 模式
     */
    private final String mode;

    /**
     * 策略
     */
    private final String strategy;

    /**
     * 获取策略
     * @param mode 模式
     * @return 策略名
     */
    public static String getStrategy(String mode) {
        for (UploadModeEnum uploadModeEnum : UploadModeEnum.values()) {
            if (uploadModeEnum.getMode().equals(mode)) {
                return uploadModeEnum.getStrategy();
            }
        }
        return null;
    }
}
