package com.arno.tech.spring.chatgpt.ai.model;

import com.arno.tech.spring.chatgpt.ai.model.aggregates.AIAnswer;
import com.arno.tech.spring.chatgpt.ai.model.chat.ChatModelResponse;

import java.util.function.Consumer;

/**
 * AI Model
 * @author xuxin14
 * @since 2023/03/02
 */
public interface IAIModel {
    void doText3(String openAiKey, String question, Consumer<AIAnswer> aiAnswerConsumer);

    void doChatByTurbo(String openAiKey, String question, Consumer<ChatModelResponse> consumer);
}