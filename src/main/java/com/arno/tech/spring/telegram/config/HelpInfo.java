package com.arno.tech.spring.telegram.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 帮助信息
 *
 * @author ArnoFrost
 * @since 2023/03/04
 */
@Component
public class HelpInfo {
    Map<String, String> rawMap = new HashMap<>();
    Map<String, String> adminRawMap = new HashMap<>();


    private static String HELP_STR = "";
    private static String ADMIN_HELP_STR = "";
    public HelpInfo() {
        /**
         * start - 开始
         * help - 帮助
         * text - text-davinci-003模型
         * text_help - text-davinci-003模型使用说明
         * gpt - gpt-3.5-turbo模型
         * gpt_help - gpt-3.5-turbo模型使用说明
         * gpt_clear - 清空gpt-3.5-turbo聊天记录
         */
        rawMap.put(ChatBotCommand.START, "开始");
        rawMap.put(ChatBotCommand.HELP, "帮助");
        rawMap.put(ChatBotCommand.CHAT_TEXT, "text-davinci-003模型");
        rawMap.put(ChatBotCommand.CHAT_TEXT_HELP, "text-davinci-003模型使用说明");
        rawMap.put(ChatBotCommand.CHAT_GPT, "gpt-3.5-turbo模型");
        rawMap.put(ChatBotCommand.CHAT_GPT_HELP, "gpt-3.5-turbo模型使用说明");
        rawMap.put(ChatBotCommand.CHAT_GPT_CLEAR, " 清空gpt-3.5-turbo聊天记录");



        /**
         * ```telegram command
         * register - 注册
         * delete_user - 删除用户
         * ban_user - 禁用账户
         * unban_user - 解封账户
         * get_user_list - 获取用户列表
         * fetch_log - 获取日志
         * help - 获得帮助
         * ```
         */

        adminRawMap.put(ChatBotCommand.AdminCommand.REGISTER, "注册");
        adminRawMap.put(ChatBotCommand.AdminCommand.DELETE_USER, "删除用户");
        adminRawMap.put(ChatBotCommand.AdminCommand.BAN_USER, "禁用账户");
        adminRawMap.put(ChatBotCommand.AdminCommand.UNBAN_USER, "解封账户");
        adminRawMap.put(ChatBotCommand.AdminCommand.GET_USER_LIST, "获取用户列表");
        adminRawMap.put(ChatBotCommand.AdminCommand.FETCH_LOG, "获取日志");
        adminRawMap.put(ChatBotCommand.AdminCommand.HELP, "获得帮助");


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("目前支持的命令有: \n");
        rawMap.forEach((k, v) -> {
            stringBuilder.append(k).append(" - ").append(v).append("\n");
        });
        HELP_STR = stringBuilder.toString();

        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("目前支持的命令有: \n");
        adminRawMap.forEach((k, v) -> {
            stringBuilder2.append(k).append(" - ").append(v).append("\n");
        });
        ADMIN_HELP_STR =  stringBuilder2.toString();
    }

    public String getHelpString() {
        return HELP_STR;
    }

    public String getAdminHelpString() {
        return ADMIN_HELP_STR;
    }
}
