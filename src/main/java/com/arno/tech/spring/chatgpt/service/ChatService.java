package com.arno.tech.spring.chatgpt.service;

import com.arno.tech.spring.chatgpt.config.mode.GptMode;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * 聊天服务
 *
 * @author xuxin14
 * @since 2023/02/07
 */
public interface ChatService {
    /**
     * 同步请求
     *
     * @param question
     * @return {@link String}
     * @throws IOException
     */
    String doChat(String question) throws IOException;

    /**
     * 异步请求
     *
     * @param question
     * @param consumer
     * @throws IOException
     */
    void doChat(String question, Consumer<String> consumer);


    void doChat(String question, GptMode mode, Consumer<String> consumer);
}
