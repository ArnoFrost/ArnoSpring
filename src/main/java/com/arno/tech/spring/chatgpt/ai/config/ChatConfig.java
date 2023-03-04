package com.arno.tech.spring.chatgpt.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 应用配置
 *
 * @author ArnoFrost
 * @since 2023/02/02
 */
@Data
@Component
@ConfigurationProperties(prefix = "chatbot-api.okhttp")
public class ChatConfig {
    /**
     * 请求服务器地址
     */
    String host;

    //region 请求性能配置
    /**
     * 连接池大小，指单个okhttpclient实例所有连接的连接池
     */
    private int maxIdleConnections;
    /**
     * 连接池中连接的最大时长
     */
    private int keepAliveDuration;
    /**
     * 连接的最大超时时间
     */
    private int connectTimeout;

    /**
     * 读超时
     */
    private int readTimeout;
    /**
     * 写超时
     */
    private int writeTimeout;

    /**
     * 当前okhttpclient实例最大的并发请求数
     */
    private int maxRequests;
    /**
     * 单个主机最大请求并发数，这里的主机指被请求方主机，一般可以理解对调用方有限流作用。注意：websocket请求不受这个限制。
     */
    private int maxRequestPerHost;
    //endregion
}
