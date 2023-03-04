package com.arno.tech.spring.user.model.bean;

import lombok.Data;
import lombok.ToString;

/**
 * 用户角色
 *
 * @author ArnoFrost
 * @since 2023/03/04
 */
@Data
@ToString
public class User {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 状态
     */
    private int status;

    /**
     * 角色
     */
    private int role;


}
