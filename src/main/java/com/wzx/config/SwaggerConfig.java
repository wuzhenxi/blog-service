package com.wzx.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swaggger2配置类.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2020/8/18</pre>
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value(value = "${swagger.base.path:}")
    private String basePath;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("blog-server")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wzx"))
                .paths(PathSelectors.any())
                .build().pathMapping(basePath);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Jessewu-blog")
                .description("JesseWu's blog for java")
                .termsOfServiceUrl("https://www.wuzhenxicloud.com:8081/doc.html")
                .contact(new Contact("Jessewu", "https://www.wuzhenxicloud.com", "wyyxwzx@163.com"))
                .version("1.0")
                .build();
    }

}
