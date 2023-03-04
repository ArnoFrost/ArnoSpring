package com.arno.tech.spring.user.model;

import com.arno.tech.spring.user.model.bean.User;

import java.util.List;

/**
 * 用户数据
 *
 * @author ArnoFrost
 * @since 2023/03/04
 */
public interface IUserModel {

    User getUserInfo(long id);

    boolean addUser(User user);

    boolean deleteUser(long id);

    boolean changeUserStatus(long id, int status);

    List<User> getUserList();
}
