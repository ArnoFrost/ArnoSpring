package com.arno.tech.spring.chatgpt.ai.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author ArnoFrost
 * @since 2023/03/02
 */
@Configuration
public class OkHttpFactory {

    @Bean
    public okhttp3.OkHttpClient okHttpClient(ChatConfig config){
        return (new OkHttpClient.Builder())
                .connectionPool(new ConnectionPool(config.getMaxIdleConnections(), config.getKeepAliveDuration(), TimeUnit.SECONDS))
                .connectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(config.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(config.getWriteTimeout(), TimeUnit.SECONDS)
                .build();
    }
}
