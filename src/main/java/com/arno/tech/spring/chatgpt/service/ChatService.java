package com.arno.tech.spring.chatgpt.service;

import com.arno.tech.spring.chatgpt.ai.OpenAiResult;
import com.arno.tech.spring.chatgpt.ai.vo.ChatVo;

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
}
