package com.arno.tech.spring.user.service;

import com.arno.tech.spring.user.model.IUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
