package com.arno.tech.spring.user.model;

import com.arno.tech.spring.user.model.bean.User;

import javax.validation.constraints.NotNull;
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

    boolean addUser(List<User> userList);

    boolean deleteUser(long id);

    boolean changeUserStatus(long id, int status);

    @NotNull List<User> getUserList();

    boolean dropAll();

}
