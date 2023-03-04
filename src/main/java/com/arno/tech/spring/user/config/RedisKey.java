package com.arno.tech.spring.user.config;

/**
 * Redis Key
 *
 * @author ArnoFrost
 * @since 2023/03/04
 */
public interface RedisKey {
    /**
     * 用户信息 格式 arno:telegram:chat:user_info:{id}
     */
    String USER_INFO = "arno:telegram:chat:user_info:";
}
