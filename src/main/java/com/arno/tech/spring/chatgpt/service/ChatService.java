package com.arno.tech.spring.chatgpt.service;

import com.arno.tech.spring.chatgpt.ai.model.chat.ChatModelResponse;
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
     * 异步请求
     *
     * @param question
     * @param consumer
     * @throws IOException
     */
    void doText(String question, Consumer<String> consumer);


    /**
     * 按照模式异步请求
     *
     * @param question
     * @param mode
     * @param consumer
     */
    void doChatByMode(String question, GptMode mode, Consumer<ChatModelResponse> consumer);

    /**
     * 按照模式异步请求
     *
     * @param question
     * @param consumer
     */
    void doChatByTurbo(String question, Consumer<String> consumer);
}
