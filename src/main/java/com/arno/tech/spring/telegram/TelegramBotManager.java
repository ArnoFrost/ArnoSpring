package com.arno.tech.spring.telegram;

import com.arno.tech.spring.telegram.config.TgConfig;
import com.arno.tech.spring.telegram.service.ChatAdminBotService;
import com.arno.tech.spring.telegram.service.ChatBotService;
import com.arno.tech.spring.telegram.service.IChatAdminBotService;
import com.arno.tech.spring.telegram.service.IChatBotService;
import com.arno.tech.spring.telegram.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Telegram 管理
 *
 * @author xuxin14
 * @since 2023/02/07
 */
@Component
public class TelegramBotManager {
    private final TgConfig config;
    private final LogUtils logUtils;

    private final IChatBotService chatBotService;

    private final IChatAdminBotService adminBotService;

    @Autowired
    public TelegramBotManager(TgConfig config, ChatBotService chatBotService, ChatAdminBotService adminBotService, LogUtils logUtils) {
        this.config = config;
        this.chatBotService = chatBotService;
        this.adminBotService = adminBotService;
        this.logUtils = logUtils;
    }

    /**
     * 初始化配置
     */
    public void init() {
        chatBotService.init();
        adminBotService.init();
    }


}
