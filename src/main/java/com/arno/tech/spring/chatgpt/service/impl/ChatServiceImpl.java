package com.arno.tech.spring.chatgpt.service.impl;

import com.arno.tech.spring.chatgpt.ai.IOpenAI;
import com.arno.tech.spring.chatgpt.ai.OpenAiResult;
import com.arno.tech.spring.chatgpt.ai.model.chat.ChatModelResponse;
import com.arno.tech.spring.chatgpt.ai.vo.ChatVo;
import com.arno.tech.spring.chatgpt.config.mode.GptMode;
import com.arno.tech.spring.chatgpt.service.ChatService;
import com.arno.tech.spring.telegram.model.bean.Chat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 聊天服务
 *
 * @author xuxin14
 * @since 2023/02/02
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
    public void doText(String question, OpenAiResult<String> consumer) {
        openAI.doByText3(openAiKey, question, consumer);
    }

    @Override
    public void doChatByTurbo(String question, OpenAiResult<ChatVo> consumer) {
        openAI.doChatByTurbo(openAiKey, question, (result, msg) -> {
            if (result == null) {
                consumer.onResult(null, msg);
            } else {
                ChatVo chatVo = new ChatVo();
                BeanUtils.copyProperties(result, chatVo);
                consumer.onResult(chatVo, msg);
            }
        });
    }

    @Override
    public void doChatByTurbo(List<Chat> chats, OpenAiResult<ChatVo> consumer) {
        openAI.doChatByTurbo(openAiKey, chats, (result, msg) -> {
            if (result == null) {
                consumer.onResult(null, msg);
            } else {
                ChatVo chatVo = new ChatVo();
                BeanUtils.copyProperties(result, chatVo);
                consumer.onResult(chatVo, msg);
            }
        });
    }
}
