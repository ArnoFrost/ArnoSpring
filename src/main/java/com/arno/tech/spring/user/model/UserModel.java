package com.arno.tech.spring.user.model;

import com.arno.tech.spring.base.service.CacheService;
import com.arno.tech.spring.base.service.ICacheService;
import com.arno.tech.spring.base.utils.JacksonUtils;
import com.arno.tech.spring.base.utils.LogUtils;
import com.arno.tech.spring.user.config.RedisKey;
import com.arno.tech.spring.user.model.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public UserModel(CacheService cacheService) {
        this.cacheService = cacheService;
        this.logUtils = LogUtils.getInstance();
    }

    //region 共有方法
    @Override
    public User getUserInfo(long id) {
        List<User> userInfoFromCache = getUserInfoFromCache();
        if (userInfoFromCache == null || userInfoFromCache.isEmpty()) {
            logUtils.log(LogUtils.LogLevel.WARN, "getUserInfo", -1L, "not find user");
            return null;
        }
        User user = userInfoFromCache.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);

        logUtils.log(LogUtils.LogLevel.INFO, "getUserInfo", -1L, "user:" + user);
        return user;
    }

    @Override
    public boolean addUser(User user) {
        //region 健壮性维护
        if (user == null) {
            logUtils.log(LogUtils.LogLevel.WARN, "addUser", -1L, "user is null");
            return false;
        }
        if (user.getId() == 0) {
            logUtils.log(LogUtils.LogLevel.WARN, "addUser", -1L, "user id is 0 user = " + user);
            return false;
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            logUtils.log(LogUtils.LogLevel.WARN, "addUser", -1L, "user name is null user = " + user);
            return false;
        }
        if (user.getRole() <= 0) {
            logUtils.log(LogUtils.LogLevel.WARN, "addUser", -1L, "user role is illegal user = " + user);
            return false;
        }
        //endregion


        List<User> userList = getUserList();
        if (userList == null || userList.isEmpty()) {
            userList = new ArrayList<>();
        }

        if (userList.stream().anyMatch(u -> Objects.equals(u.getId(), user.getId()))) {
            logUtils.log(LogUtils.LogLevel.WARN, "addUser", -1L, "user is exist");
            return false;
        }
        userList.add(user);
        cacheService.setString(RedisKey.USER_INFO, JacksonUtils.beanToString(userList));
        return true;
    }

    @Override
    public boolean addUser(List<User> userList) {
        if (userList == null || userList.isEmpty()) {
            logUtils.log(LogUtils.LogLevel.WARN, "addUser", -1L, "user list is null");
            return false;
        }
        List<User> userListFromCache = getUserList();
        if (userListFromCache == null || userListFromCache.isEmpty()) {
            userListFromCache = new ArrayList<>();
        }
        List<User> finalUserListFromCache = userListFromCache;
        List<User> collect = userList.stream()
                .filter(u -> !finalUserListFromCache.stream().anyMatch(u1 -> Objects.equals(u1.getId(), u.getId())))
                .collect(Collectors.toList());
        if (collect.isEmpty()) {
            logUtils.log(LogUtils.LogLevel.WARN, "addUser", -1L, "user list is exist");
            return false;
        }
        userListFromCache.addAll(collect);
        cacheService.setString(RedisKey.USER_INFO, JacksonUtils.beanToString(userListFromCache));
        return true;
    }

    @Override
    public boolean deleteUser(long id) {
        List<User> userList = getUserList();
        if (userList == null || userList.isEmpty()) {
            logUtils.log(LogUtils.LogLevel.WARN, "deleteUser", -1L, "user list is null");
            return false;
        }
        boolean b = userList.removeIf(u -> u.getId() == id);
        if (b) {
            cacheService.setString(RedisKey.USER_INFO, JacksonUtils.beanToString(userList));
        }
        return true;
    }

    @Override
    public boolean changeUserStatus(long id, int status) {
        List<User> userList = getUserList();
        if (userList == null || userList.isEmpty()) {
            logUtils.log(LogUtils.LogLevel.WARN, "changeUserStatus", -1L, "user list is null");
            return false;
        }
        List<User> updatedUsers = userList.stream()
                .map(u -> {
                    u.setStatus(status);
                    return u;
                })
                .collect(Collectors.toList());
// 将更新后的列表保存回去
        return save(updatedUsers);
    }

    @Override
    public @NotNull List<User> getUserList() {
        List<User> cache = getUserInfoFromCache();
        if (cache == null) {
            return new ArrayList<>();
        }
        return cache;
    }

    @Override
    public boolean dropAll() {
        return cacheService.delete(RedisKey.USER_INFO);
    }
    //endregion

    //region 私有方法
    //增加本地缓存逻辑
    private List<User> getUserInfoFromCache() {
        String localCache = cacheService.getLocalStr(RedisKey.USER_INFO);
        if (localCache != null && !localCache.isEmpty()) {
            logUtils.log(LogUtils.LogLevel.INFO, "getUserInfoFromCache", -1L, "hint local cache");
            return JacksonUtils.stringToList(localCache, User.class);
        }
        String string = cacheService.getString(RedisKey.USER_INFO);
        if (string == null || string.isEmpty()) {
            logUtils.log(LogUtils.LogLevel.WARN, "getUserInfoFromCache", -1L, "not find user");
            cacheService.setLocalStr(RedisKey.USER_INFO, "[]");
            return null;
        }
        logUtils.log(LogUtils.LogLevel.INFO, "getUserInfoFromCache", -1L, "hint redis cache");
        cacheService.setLocalStr(RedisKey.USER_INFO, string);
        return JacksonUtils.stringToList(string, User.class);
    }

    private boolean save(List<User> updatedUsers) {
        String string = JacksonUtils.beanToString(updatedUsers);
        cacheService.setString(RedisKey.USER_INFO, string);
        cacheService.setLocalStr(RedisKey.USER_INFO, string);
        return true;
    }
    //endregion
}
