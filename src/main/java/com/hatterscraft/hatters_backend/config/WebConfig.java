package com.hatterscraft.hatters_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Разрешаем доступ к файлам по пути /static/skins/**
        registry.addResourceHandler("/static/skins/**")
                // "file:" указывает, что путь на диске — это файловая система, а не classpath
                .addResourceLocations("file:uploads/skins/")
                // Чтобы браузер не кэшировал при тестировании
                .setCachePeriod(0);
    }
}