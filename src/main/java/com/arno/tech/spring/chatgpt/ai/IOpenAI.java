package com.arno.tech.spring.chatgpt.ai;

import com.arno.tech.spring.chatgpt.ai.model.chat.ChatModelResponse;
import com.arno.tech.spring.chatgpt.config.mode.GptMode;
import com.arno.tech.spring.telegram.model.bean.Chat;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * OpenAI 接口
 *
 * @author xuxin14
 * @since 2023/02/07
 */
public interface IOpenAI {

    /**
     * text 文字对话 异步
     *
     * @param openAiKey
     * @param question
     * @param consumer
     */
    void doByText3(String openAiKey, String question, @NotNull OpenAiResult<String> consumer);

    /**
     * Chatgpt 简单对话
     *
     * @param openAiKey
     * @param question
     * @param consumer
     */
    void doChatByTurbo(String openAiKey, String question, @NotNull OpenAiResult<ChatModelResponse> consumer);

    /**
     * Chatgpt 多轮对话
     *
     * @param openAiKey 人工智能钥匙开门
     * @param chats     聊天
     * @param consumer  消费者
     */
    void doChatByTurbo(String openAiKey, List<Chat> chats, OpenAiResult<ChatModelResponse> consumer);
}
