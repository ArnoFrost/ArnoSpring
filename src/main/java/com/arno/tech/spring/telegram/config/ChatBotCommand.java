package com.arno.tech.spring.telegram.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 聊天机器人 命令
 *
 * @author ArnoFrost
 * @since 2023/02/07
 */
public interface ChatBotCommand {

    /**
     * ```telegram command
     * start - 开始
     * help - 帮助
     * text - text-davinci-003模型
     * text_help - text-davinci-003模型使用说明
     * gpt - gpt-3.5-turbo模型
     * gpt_help - gpt-3.5-turbo模型使用说明
     * gpt_clear - 清空gpt-3.5-turbo聊天记录
     * ```
     */
    String START = "/start";
    String HELP = "/help";
    String CHAT_TEXT = "/text";
    String CHAT_TEXT_HELP = "/text_help";
    String CHAT_GPT = "/gpt";
    String CHAT_GPT_SIMPLE = "/";
    String CHAT_GPT_HELP = "/gpt_help";
    String REGISTER = "/register";
    String CHAT_GPT_CLEAR = "/gpt_clear";


    interface INFO {
        String CHAT_TEXT_HELP_INFO = "输入 " + CHAT_TEXT + "你想说的话，例如：" + CHAT_TEXT + " 你好";
        String CHAT_GPT_HELP_INFO =
                "输入" + CHAT_GPT + " + 你想说的话，例如：" + CHAT_GPT + " 你能做什么?\n" +
                        "也可以使用简化命令 " + CHAT_GPT_SIMPLE + "你想说的话, 例如：" + CHAT_GPT_SIMPLE + "你能做什么?\n" +
                        "支持多轮对话~ 欢迎使用~\n" +
                        "使用" + CHAT_GPT_CLEAR + " 清空聊天记录\n";

        String SLIPT_STRING = "         ------------------          ";
//        String HELP_INFO = "目前支持的命令有: \n" +
//                ChatBotCommand.START + SLIPT_STRING + "             开始\n" +
//                ChatBotCommand.HELP + SLIPT_STRING + "              帮助\n" +
//                ChatBotCommand.CHAT_TEXT + SLIPT_STRING + "         text-davinci-003模型\n" +
//                ChatBotCommand.CHAT_TEXT_HELP + SLIPT_STRING + "    text-davinci-003模型使用说明\n" +
//                ChatBotCommand.CHAT_GPT + SLIPT_STRING + "          gpt-3.5-turbo模型\n" +
//                ChatBotCommand.CHAT_GPT_HELP + SLIPT_STRING + "     gpt-3.5-turbo模型使用说明\n" +
//                ChatBotCommand.CHAT_GPT_CLEAR + SLIPT_STRING + "    清空gpt-3.5-turbo聊天记录\n";

        String START_INFO = "欢迎使用聊天机器人，你可以使用 " + ChatBotCommand.HELP + " 命令查看支持的命令";

        //TODO:ArnoFrost 2023/3/4
//        String REGISTER_INFO = "注册成功，你可以使用 " + ChatBotCommand.CHAT_GPT + " 命令使用gpt-3.5-turbo模型聊天了";
        String REGISTER_INFO = "白名单机制开启,请向管理员Arno 反馈本次会话 id : \n";
        String UNKNOWN_INFO = "未知命令";

        String CLEAR_INFO_ERROR = "删除失败，请重试";
        String CLEAR_INFO_SUCCESS = "已删除聊天记录，请开始新一轮对话\nHave fun~";
        String QUESTION_IS_NULL = "请输入内容";

    }

    interface AdminCommand {
        String REGISTER = "/register";
        String DELETE_USER = "/delete_user";
    }
}


