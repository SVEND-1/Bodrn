package org.example.bodrn.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/", "file:./target/uploads/");

        registry.addResourceHandler("/css/**", "/js/**", "/images/**")
                .addResourceLocations("classpath:/static/css/",
                        "classpath:/static/js/",
                        "classpath:/static/images/");
    }
}
