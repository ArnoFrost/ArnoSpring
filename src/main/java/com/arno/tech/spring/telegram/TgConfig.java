package com.arno.tech.spring.telegram;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Telegram 配置处理
 *
 * @author xuxin14
 * @since 2023/02/07
 */
@Data
@Component
@ConfigurationProperties(prefix = "telegram.bot")
public class TgConfig {

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


    /**
     * 获取用户名
     *
     * @param chatId 对话id
     * @return {@link String}
     */
    public String getUserNameByChatId(Long chatId) {
        if (chatId == null) {
            return null;
        }
        return whiteMap.get(chatId.toString());
    }

    /**
     * 是否在白名单中
     *
     * @param chatId 对话id
     * @return {@link boolean}
     */
    public boolean isInWhiteList(Long chatId) {
        if (chatId == null) {
            return false;
        }
        if (whiteEnable) {
            return whiteMap.containsKey(chatId.toString());
        }
        return true;
    }
}
