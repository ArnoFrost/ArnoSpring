package com.arno.tech.spring.telegram;

/**
 * 聊天机器人 命令
 *
 * @author xuxin14
 * @since 2023/02/07
 */
public interface ChatBotCommand {

    String START = "/start";
    String HELP = "/help";
    String CHAT_GPT = "/chatgpt";
    String CHAT_GPT_HELP = "/chatgpt_help";
    String CHAT_GPT_HELP_INFO = "输入 /chat_gpt + 你想说的话，例如：/chat_gpt 你好";
    String CHAT_GPT_REGISTER = "/chatgpt_register";

    String CHAT_WEATHER = "/chat_weather";
}
