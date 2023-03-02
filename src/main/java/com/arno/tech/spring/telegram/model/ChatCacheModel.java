package com.arno.tech.spring.telegram.model;

import com.arno.tech.spring.base.utils.JacksonUtils;
import com.arno.tech.spring.telegram.config.RedisKey;
import com.arno.tech.spring.telegram.model.bean.Chat;
import jodd.util.StringUtil;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 对话缓存服务
 *
 * @author xuxin14
 * @since 2023/03/02
 */
@Repository
public class ChatCacheModel implements IChatCacheModel {

    private final RedissonClient redissonClient;

    @Autowired
    public ChatCacheModel(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean addChat(String uid, String role, String content) {
        List<Chat> chat = getChat(uid);
        if (chat == null || chat.isEmpty()) {
            chat = new ArrayList<>();
        }
        chat.add(new Chat(role, content, System.currentTimeMillis()));
        setString(RedisKey.KEY_PREFIX + uid, JacksonUtils.beanToString(chat));
        return true;
    }

    @Override
    public boolean deleteChat(String uid) {
        return delete(uid);
    }

    @Override
    public List<Chat> getChat(String uid) {
        String temp = getString(uid);
        if (StringUtil.isNotEmpty(temp)) {
            return JacksonUtils.stringToBean(temp, List.class);
        }
        return null;
    }

    private void setString(String key, String value) {
        redissonClient.getBucket(key).set(value);
    }

    private String getString(String key) {
        return redissonClient.getBucket(key).get().toString();
    }

    private boolean delete(String key) {
        return redissonClient.getBucket(key).delete();
    }
}
