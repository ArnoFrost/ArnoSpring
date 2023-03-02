package com.arno.tech.spring.chatgpt.service;

import com.arno.tech.spring.chatgpt.ai.OpenAiResult;
import com.arno.tech.spring.chatgpt.ai.vo.ChatVo;
import com.arno.tech.spring.telegram.model.bean.Chat;

import java.util.List;

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
     */
    void doText(String question, OpenAiResult<String> consumer);


    /**
     * 按照模式异步请求
     *
     * @param question
     * @param consumer
     */
    void doChatByTurbo(String question, OpenAiResult<ChatVo> consumer);

    /**
     * Turbo聊天多轮对话支持
     *
     * @param chats    聊天
     * @param consumer 消费者
     */
    void doChatByTurbo(List<Chat> chats, OpenAiResult<ChatVo> consumer);
}
