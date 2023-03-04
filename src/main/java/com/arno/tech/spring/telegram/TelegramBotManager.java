package com.arno.tech.spring.telegram;

import com.arno.tech.spring.base.utils.LogUtils;
import com.arno.tech.spring.telegram.service.ChatAdminBotService;
import com.arno.tech.spring.telegram.service.ChatBotService;
import com.arno.tech.spring.telegram.service.IChatAdminBotService;
import com.arno.tech.spring.telegram.service.IChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Telegram 管理
 *
 * @author ArnoFrost
 * @since 2023/02/07
 */
@Component
public class TelegramBotManager {
    private final LogUtils logUtils;

    private final IChatBotService chatBotService;

    private final IChatAdminBotService adminBotService;

    @Autowired
    public TelegramBotManager(ChatBotService chatBotService, ChatAdminBotService adminBotService) {
        this.chatBotService = chatBotService;
        this.adminBotService = adminBotService;
        this.logUtils = LogUtils.getInstance();
    }

    /**
     * 初始化配置
     */
    public void init() {
        logUtils.log(LogUtils.LogLevel.INFO, "TelegramBotManager", 0L, "init");
        chatBotService.init();
        adminBotService.init();
    }


}
