package com.arno.tech.spring.chatgpt.service.impl;

import com.arno.tech.spring.chatgpt.ai.IOpenAI;
import com.arno.tech.spring.chatgpt.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * 聊天服务
 *
 * @author xuxin14
 * @date 2023/02/02
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
    @Value("${chatbot-api.key}")
    private String openAiKey;
    private final IOpenAI openAI;

    @Autowired
    public ChatServiceImpl(IOpenAI openAI) {
        this.openAI = openAI;
    }


    @Override
    public String doChat(String question) {
        return openAI.doChatGPT(openAiKey, question);
    }

    @Override
    public void doChat(String question, Consumer<String> consumer) {
        openAI.doChatGPT(openAiKey, question, consumer);
    }
}
