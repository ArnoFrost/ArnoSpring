package com.arno.tech.spring.user.service;

import com.arno.tech.spring.user.model.bean.User;

import java.util.List;

/**
 * 用户信息服务
 *
 * @author ArnoFrost
 * @since 2023/03/04
 */
public interface IUserInfoService {

    List<User> getUserList();

    boolean registerUser(User user);

    boolean deleteUser(long id);

    boolean banUser(long id);

    boolean unBanUser(long id);

    boolean isAdmin(long id);

    boolean isValidUser(Long id);

    String getUserNameByChatId(Long chatId);
}