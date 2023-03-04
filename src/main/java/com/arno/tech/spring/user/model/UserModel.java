package com.arno.tech.spring.user.model;

import com.arno.tech.spring.base.service.CacheService;
import com.arno.tech.spring.base.service.ICacheService;
import com.arno.tech.spring.base.utils.JacksonUtils;
import com.arno.tech.spring.telegram.utils.LogUtils;
import com.arno.tech.spring.user.config.RedisKey;
import com.arno.tech.spring.user.model.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户数据
 *
 * @author ArnoFrost
 * @since 2023/03/04
 */
@Repository
public class UserModel implements IUserModel {
    private final ICacheService cacheService;

    private final LogUtils logUtils;

    @Autowired
    public UserModel(CacheService cacheService, LogUtils logUtils) {
        this.cacheService = cacheService;
        this.logUtils = logUtils;
    }

    @Override
    public User getUserInfo(long id) {
        List<User> userInfoFromCache = getUserInfoFromCache(id);
        if (userInfoFromCache == null || userInfoFromCache.isEmpty()) {
            logUtils.log(LogUtils.LogLevel.WARN, "getUserInfo", -1L, "not find user", null);
            return null;
        }
        User user = userInfoFromCache.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);

        logUtils.log(LogUtils.LogLevel.INFO, "getUserInfo", -1L, "user:" + user, null);
        return user;
    }

    @Override
    public boolean addUser(User user) {
        //region 健壮性维护
        if (user == null) {
            logUtils.log(LogUtils.LogLevel.WARN, "addUser", -1L, "user is null", null);
            return false;
        }
        if (user.getId() == 0) {
            logUtils.log(LogUtils.LogLevel.WARN, "addUser", -1L, "user id is 0 user = " + user, null);
            return false;
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            logUtils.log(LogUtils.LogLevel.WARN, "addUser", -1L, "user name is null user = " + user, null);
            return false;
        }
        if (user.getRole() <= 0) {
            logUtils.log(LogUtils.LogLevel.WARN, "addUser", -1L, "user role is illegal user = " + user, null);
            return false;
        }
        //endregion


        List<User> userList = getUserList();
        if (userList == null || userList.isEmpty()) {
            userList = new ArrayList<>();
        }

        if (userList.stream().anyMatch(u -> Objects.equals(u.getId(), user.getId()))) {
            logUtils.log(LogUtils.LogLevel.WARN, "addUser", -1L, "user is exist", null);
            return false;
        }
        userList.add(user);
        cacheService.setString(RedisKey.USER_INFO, JacksonUtils.beanToString(userList));
        return true;
    }

    @Override
    public boolean deleteUser(long id) {
        return false;
    }

    @Override
    public boolean changeUserStatus(long id, int status) {
        return false;
    }

    @Override
    public List<User> getUserList() {
        return null;
    }

    private List<User> getUserInfoFromCache(long id) {
        String string = cacheService.getString(RedisKey.USER_INFO + id);
        if (string == null || string.isEmpty()) {
            return null;
        }
        return JacksonUtils.stringToList(string, User.class);
    }
}
