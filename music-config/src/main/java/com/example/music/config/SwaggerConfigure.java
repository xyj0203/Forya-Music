package com.example.music.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "swagger",name = "swagger-enable",havingValue = "true")
public class SwaggerConfigure {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Music API")
                .description(" API By Xuyujie")
                .termsOfServiceUrl("2582504569@qq.com")
                .version("1.0")
                .build();
    }

    @Bean
    public Docket createRestApiForAdmin() {
        return new Docket(DocumentationType.SWAGGER_2).enable(true).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.example.music.Controller"))
                .paths(PathSelectors.any()).build().groupName("管理员管理");
    }
}
