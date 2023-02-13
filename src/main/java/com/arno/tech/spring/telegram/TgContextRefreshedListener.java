package com.arno.tech.spring.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * TG 启动运行器
 *
 * @author xuxin14
 * @since 2023/02/07
 */
@Component
public class TgContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * 环境配置
     */
    private final TelegramManager telegramManager;

    @Autowired
    public TgContextRefreshedListener(TelegramManager telegramManager) {
        this.telegramManager = telegramManager;
    }

    /**
     * spring 启动后接收事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        telegramManager.init();
    }
}
