package com.arno.tech.spring.user.model;

import com.arno.tech.spring.telegram.config.TgConfig;
import com.arno.tech.spring.user.model.bean.Role;
import com.arno.tech.spring.user.model.bean.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IUserModelTest {

    @Autowired
    public UserModel userModel;

    @Autowired
    public TgConfig config;

    private final List<User> userList = new ArrayList<>();

    @Test
    public void init() {
        buildUserList();
        printMap();
    }

    private void buildUserList() {
        config.getWhiteMap().forEach((id, name) -> {
            User user = new User();
            user.setId(Long.parseLong(id));
            user.setName(name);
            user.setStatus(1);
            user.setRole(Role.USER);
            userList.add(user);
        });
    }

    private void printMap() {
        config.getWhiteMap().forEach((k, v) -> {
            System.out.println(k + " " + v);
        });
    }

    @Test
    void getUserInfo() {
        Long id = 0L;
        User user = userModel.getUserInfo(id);
        assert user != null;
    }

    @Test
    void addUser() {
        User user = new User();
        user.setId(666L);
        user.setName("test");
        user.setStatus(1);
        user.setRole(Role.USER);
        boolean b = userModel.addUser(user);
        assert b;
    }

    @Test
    void changeUserStatus() {
        userModel.changeUserStatus(666L, 0);
        assert userModel.getUserInfo(666L).getStatus() == 0;
    }

    @Test
    void getUserList() {
        List<User> userList = userModel.getUserList();
        assert userList.size() > 0;
    }

//    @Test
void deleteUser() {
    userModel.deleteUser(666L);
    assert userModel.getUserInfo(666L) == null;
}
}