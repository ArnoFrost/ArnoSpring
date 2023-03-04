package com.arno.tech.spring.telegram;

import com.arno.tech.spring.telegram.config.TgConfig;
import com.arno.tech.spring.telegram.utils.LogUtils;
import com.arno.tech.spring.user.service.IUserInfoService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * TG 启动运行器
 *
 * @author ArnoFrost
 * @since 2023/02/07
 */
@Component
public class TgContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * 环境配置
     */
    private final TelegramBotManager telegramBotManager;

    private final TgConfig config;

    private final IUserInfoService userInfoService;

    @Autowired
    public TgContextRefreshedListener(TelegramBotManager telegramBotManager, TgConfig config, IUserInfoService userInfoService) {
        this.telegramBotManager = telegramBotManager;
        this.config = config;
        this.userInfoService = userInfoService;
    }

    /**
     * spring 启动后接收事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        telegramBotManager.init();
        LogUtils.getInstance().setConfig(config);
        LogUtils.getInstance().setUserInfoService(userInfoService);
    }
}
