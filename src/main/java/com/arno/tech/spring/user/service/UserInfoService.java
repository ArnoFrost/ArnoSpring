package com.arno.tech.spring.user.service;

import com.arno.tech.spring.user.model.IUserModel;
import com.arno.tech.spring.user.model.bean.Role;
import com.arno.tech.spring.user.model.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户信息服务实现
 *
 * @author ArnoFrost
 * @since 2023/03/04
 */
@Service
public class UserInfoService implements IUserInfoService {
    private final IUserModel userModel;

    @Autowired
    public UserInfoService(IUserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public List<User> getUserList() {
        return userModel.getUserList();
    }

    @Override
    public boolean registerUser(User user) {
        return userModel.addUser(user);
    }

    @Override
    public boolean deleteUser(long id) {
        return userModel.deleteUser(id);
    }

    @Override
    public boolean banUser(long id) {
        return userModel.changeUserStatus(id, 1);
    }

    @Override
    public boolean unBanUser(long id) {
        return userModel.changeUserStatus(id, 0);
    }

    @Override
    public boolean isAdmin(long id) {
        User userInfo = userModel.getUserInfo(id);
        return userInfo != null && userInfo.getRole() == Role.ADMIN;
    }

    @Override
    public boolean isValidUser(Long id) {
        User userInfo = userModel.getUserInfo(id);
        return userInfo != null && userInfo.getStatus() == 0;
    }

    @Override
    public String getUserNameByChatId(Long chatId) {
        List<User> userList = userModel.getUserList();
        for (User user : userList) {
            if (user.getId().equals(chatId)) {
                return user.getName();
            }
        }
        return null;
    }
}
