package com.arno.tech.spring.telegram.model;

import com.arno.tech.spring.base.service.ICacheService;
import com.arno.tech.spring.base.utils.JacksonUtils;
import com.arno.tech.spring.telegram.config.RedisKey;
import com.arno.tech.spring.telegram.model.bean.Chat;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 对话缓存服务
 *
 * @author ArnoFrost
 * @since 2023/03/02
 */
@Repository
public class ChatCacheModel implements IChatCacheModel {

    private final ICacheService cacheService;

    @Autowired
    public ChatCacheModel(ICacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public boolean addChat(String uid, String role, String content) {
        List<Chat> chat = getChat(uid);
        if (chat == null || chat.isEmpty()) {
            chat = new ArrayList<>();
        }
        chat.add(new Chat(role, content, System.currentTimeMillis()));
        cacheService.setString(RedisKey.KEY_PREFIX + uid, JacksonUtils.beanToString(chat));
        return true;
    }

    @Override
    public boolean deleteChat(String uid) {
        cacheService.setString(RedisKey.KEY_PREFIX + uid, "");
        return true;
    }

    @Override
    public @NotNull List<Chat> getChat(String uid) {
        String temp = cacheService.getString(RedisKey.KEY_PREFIX + uid);
        if (StringUtil.isNotEmpty(temp)) {
            List<Chat> list = JacksonUtils.stringToList(temp, Chat.class);
            if (list == null) {
                return new ArrayList<>();
            } else {
                return list;
            }
        }
        return new ArrayList<>();
    }
}
