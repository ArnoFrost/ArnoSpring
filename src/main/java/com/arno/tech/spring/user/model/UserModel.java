package com.arno.tech.spring.user.model;

import com.arno.tech.spring.base.service.CacheService;
import com.arno.tech.spring.base.service.ICacheService;
import com.arno.tech.spring.user.model.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 用户数据
 *
 * @author ArnoFrost
 * @since 2023/03/04
 */
@Repository
public class UserModel implements IUserModel {
    private final ICacheService cacheService;

    @Autowired
    public UserModel(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public User getUserInfo(long id) {
        return null;
    }

    @Override
    public boolean addUserInfo(User user) {
        return false;
    }

    @Override
    public boolean deleteUser(long id) {
        return false;
    }

    @Override
    public boolean changeUserStatus(long id, int status) {
        return false;
    }
}
