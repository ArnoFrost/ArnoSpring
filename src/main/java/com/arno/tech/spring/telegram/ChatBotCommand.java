package com.arno.tech.spring.telegram;

/**
 * 聊天机器人 命令
 *
 * @author xuxin14
 * @since 2023/02/07
 */
public interface ChatBotCommand {
    /**
     * ```telegram command
     * register - 注册
     * start - 开始
     * help - 帮助
     * text - 文本聊天
     * text_help - 文本聊天帮助
     * gpt - gpt聊天
     * gpt_help - gpt聊天帮助
     * ```
     */
    String START = "/start";
    String HELP = "/help";
    String CHAT_TEXT = "/text";
    String CHAT_TEXT_HELP = "/text_help";
    String CHAT_GPT = "/gpt";
    String CHAT_GPT_HELP = "/gpt_help";
    String REGISTER = "/register";

    interface INFO {
        String CHAT_TEXT_HELP_INFO = "输入 /text + 你想说的话，例如：/text 你好";
        String CHAT_GPT_HELP_INFO = "输入 /gpt + 你想说的话，例如：/gpt 你好";
    }
}


