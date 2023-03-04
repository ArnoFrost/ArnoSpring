package com.arno.tech.spring.user.model;

import com.arno.tech.spring.user.model.bean.User;

/**
 * 用户数据
 *
 * @author ArnoFrost
 * @since 2023/03/04
 */
public interface IUserModel {

    User getUserInfo(long id);

    boolean addUserInfo(User user);

    boolean deleteUser(long id);

    boolean changeUserStatus(long id, int status);
}
