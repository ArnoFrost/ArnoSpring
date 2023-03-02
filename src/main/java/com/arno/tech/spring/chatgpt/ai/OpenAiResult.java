package com.arno.tech.spring.chatgpt.ai;

/**
 * OpenAI 请求结果回调
 *
 * @author xuxin14
 * @since 2023/03/02
 */
public interface OpenAiResult<T> {
    void onResult(T result, String msg);
}
