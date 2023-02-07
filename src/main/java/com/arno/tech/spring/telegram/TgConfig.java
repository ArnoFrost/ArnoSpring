package com.arno.tech.spring.telegram;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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

    private String token;

    private String name;
}
