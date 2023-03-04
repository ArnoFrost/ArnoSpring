package com.arno.tech.spring.telegram.config;

import lombok.Data;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Telegram 配置处理
 *
 * @author ArnoFrost
 * @since 2023/02/07
 */
@Data
@Component
@ConfigurationProperties(prefix = "telegram.bot")
public class TgConfig {

    /**
     * 调试模式
     */
    private boolean debug;

    /**
     * bot token
     */
    private String token;

    /**
     * bot name
     */
    private String name;

    /**
     * 是否开启白名单
     */
    private boolean whiteEnable;
    /**
     * 白名单
     */
    private Map<String, String> whiteMap;

    private Admin admin;


    /**
     * 获取用户名
     *
     * @param chatId 对话id
     * @return {@link String}
     */
    public @Nullable String getUserNameByChatId(Long chatId) {
        if (chatId == null) {
            return null;
        }
        return whiteMap.get(chatId.toString());
    }


    @Data
    public static class Admin {
        private String name;
        private String token;
        private String id;
    }
}
