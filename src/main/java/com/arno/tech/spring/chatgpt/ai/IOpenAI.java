package com.arno.tech.spring.chatgpt.ai;

import com.arno.tech.spring.chatgpt.ai.model.chat.ChatModelResponse;
import com.arno.tech.spring.chatgpt.config.mode.GptMode;

import javax.validation.constraints.NotNull;

/**
 * OpenAI 接口
 *
 * @author xuxin14
 * @since 2023/02/07
 */
public interface IOpenAI {

    /**
     * ChatGPT 简单对话 异步
     *
     * @param openAiKey
     * @param question
     * @param consumer
     */
    void doByText3(String openAiKey, String question, @NotNull OpenAiResult<String> consumer);

    /**
     * ChatGPT 简单对话
     *
     * @param openAiKey
     * @param question
     * @param consumer
     */
    void doChatByTurbo(String openAiKey, String question, @NotNull OpenAiResult<ChatModelResponse> consumer);
}
