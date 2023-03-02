package com.arno.tech.spring.telegram.model.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 对话记录
 *
 * @author xuxin14
 * @since 2023/03/02
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    /**
     * 角色
     */
    private String role;
    private String content;
    private long time;
}
