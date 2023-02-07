package com.arno.tech.spring.chatgpt.ai;

import javax.validation.constraints.NotNull;
import java.util.function.Consumer;

/**
 * OpenAI 接口
 *
 * @author xuxin14
 * @since 2023/02/07
 */
public interface IOpenAI {

    /**
     * ChatGPT 简单对话
     *
     * @param openAiKey
     * @param question
     * @return {@link String}
     */
    String doChatGPT(String openAiKey, String question);

    /**
     * ChatGPT 简单对话 异步
     *
     * @param openAiKey
     * @param question
     * @param consumer
     */
    void doChatGPT(String openAiKey, String question, @NotNull Consumer<String> consumer);

}
