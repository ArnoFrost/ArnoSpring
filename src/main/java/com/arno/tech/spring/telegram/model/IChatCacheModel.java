package com.arno.tech.spring.telegram.model;

import com.arno.tech.spring.telegram.model.bean.Chat;

import java.util.List;

/**
 * 对话缓存服务
 *
 * @author xuxin14
 * @since 2023/03/02
 */
public interface IChatCacheModel {
    /**
     * 添加一条聊天
     *
     * @param uid     uid
     * @param role    角色
     * @param content 内容
     * @return boolean
     */
    boolean addChat(String uid, String role, String content);

    /**
     * 删除聊天
     *
     * @param uid uid
     * @return boolean
     */
    boolean deleteChat(String uid);

    /**
     * 获取聊天
     *
     * @param uid uid
     * @return {@link List}<{@link Chat}>
     */
    List<Chat> getChat(String uid);
}
