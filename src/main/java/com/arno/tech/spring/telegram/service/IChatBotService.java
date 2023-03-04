package com.arno.tech.spring.telegram.service;

/**
 * 聊天服务
 *
 * @author ArnoFrost
 * @since 2023/03/04
 */
public interface IChatBotService {
    void init();

    boolean pushSingle(Long chatId, String msg);

    boolean pushAll(String testMsg);
}
