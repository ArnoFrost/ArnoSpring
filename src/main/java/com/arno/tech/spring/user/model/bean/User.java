package com.arno.tech.spring.user.model.bean;

import lombok.Data;

/**
 * 用户角色
 *
 * @author ArnoFrost
 * @since 2023/03/04
 */
@Data
public class User {
    /**
     * 用户名
     */
    private String name;
    /**
     * 用户id
     */
    private Long id;
    /**
     * 状态
     */
    private int status;

    /**
     * 角色
     */
    private int role;


    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", role=" + role +
                '}';
    }
}
