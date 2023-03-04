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

    /**
     * start - 开始
     * help - 帮助
     * text - text-davinci-003模型
     * text_help - text-davinci-003模型使用说明
     * gpt - gpt-3.5-turbo模型
     * gpt_help - gpt-3.5-turbo模型使用说明
     * gpt_clear - 清空gpt-3.5-turbo聊天记录
     */
    public HelpInfo() {
        rawMap.put(ChatBotCommand.START, "开始");
        rawMap.put(ChatBotCommand.HELP, "帮助");
        rawMap.put(ChatBotCommand.CHAT_TEXT, "text-davinci-003模型");
        rawMap.put(ChatBotCommand.CHAT_TEXT_HELP, "text-davinci-003模型使用说明");
        rawMap.put(ChatBotCommand.CHAT_GPT, "gpt-3.5-turbo模型");
        rawMap.put(ChatBotCommand.CHAT_GPT_HELP, "gpt-3.5-turbo模型使用说明");
        rawMap.put(ChatBotCommand.CHAT_GPT_CLEAR, " 清空gpt-3.5-turbo聊天记录");
    }

    public String getHelpString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("目前支持的命令有: \n");
        rawMap.forEach((k, v) -> {
            stringBuilder.append(k).append(" - ").append(v).append("\n");
        });
        return stringBuilder.toString();
    }
}
