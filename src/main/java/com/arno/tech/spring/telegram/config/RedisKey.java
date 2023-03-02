package com.arno.tech.spring.telegram.config;

/**
 * redis key
 *
 * @author xuxin14
 * @since 2023/03/02
 */
public interface RedisKey {
    /**
     * 方法名前缀 规则 "arno:telegram:chatgpt_bot:uid"
     */
    String KEY_PREFIX = "arno:telegram:chatgpt_bot:";
}
