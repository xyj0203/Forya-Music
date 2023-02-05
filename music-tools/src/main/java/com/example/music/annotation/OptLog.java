package com.example.music.annotation;

import java.lang.annotation.*;

/**
 * 操作日志的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptLog {

    /**
     * 操作类型
     * @return
     */
    String optType() default "";
}
