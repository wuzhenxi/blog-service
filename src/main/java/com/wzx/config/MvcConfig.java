package com.wzx.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * File Description.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2020/8/16</pre>
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file.location.path")
public class MvcConfig extends WebMvcConfigurationSupport {

    private String images;

    private String videos;

    private String other;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations(
                "file:///" + images.replace("\\", "/"));
        registry.addResourceHandler("/videos/**").addResourceLocations(
                "file:///" + videos.replace("\\", "/"));
        registry.addResourceHandler("/other/**").addResourceLocations(
                "file:///" + other.replace("\\", "/"));
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:static/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:META-INF/resources/webjars/");
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
