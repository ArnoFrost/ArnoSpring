package com.arno.tech.spring.telegram;

import com.arno.tech.spring.chatgpt.service.ChatService;
import com.arno.tech.spring.telegram.utils.LogUtils;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Telegram 管理
 *
 * @author xuxin14
 * @since 2023/02/07
 */
@Component
public class TelegramManager {
    private final ChatService chatService;
    private final TgConfig config;
    private TelegramBot bot;

    private final LogUtils logUtils;

    @Autowired
    public TelegramManager(ChatService chatService, TgConfig config) {
        this.chatService = chatService;
        this.config = config;
        logUtils = new LogUtils();
    }

    /**
     * 初始化配置
     */
    public void init() {
        if (config.isDebug()) {
            bot = new TelegramBot.Builder(config.getToken()).debug().build();
            //调试用
            logUtils.log(LogUtils.LogLevel.DEBUG, config, "TelegramManger", null, "Telegram bot init name = " + config.getName() + ", token = " + config.getToken(), null);
        } else {
            bot = new TelegramBot(config.getToken());
        }

        //注册消息监听
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::dispatchUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }

    /**
     * 分发消息
     *
     * @param update {@link Update}
     */
    private void dispatchUpdate(Update update) {
        if (update == null) {
            logUtils.log(LogUtils.LogLevel.ERROR, config, "dispatchUpdate", null, "update is null", null);
            return;
        }
        if (update.message() == null) {
            logUtils.log(LogUtils.LogLevel.ERROR, config, "dispatchUpdate", null, "update message is null", null);
            return;
        }
        if (update.message().text() == null) {
            logUtils.log(LogUtils.LogLevel.ERROR, config, "dispatchUpdate", null, "update message text is null", null);
            return;
        }
        Long chatId = update.message().chat().id();
        String text = update.message().text();
        logUtils.log(LogUtils.LogLevel.INFO, config, "dispatchUpdate", chatId, "text = " + text, null);
        String content;
        if (text.startsWith(ChatBotCommand.START)) {
            answerHelp(bot, ChatBotCommand.START, chatId);
        } else if (text.startsWith(ChatBotCommand.HELP)) {
            answerHelp(bot, ChatBotCommand.HELP, chatId);
        } else if (text.startsWith(ChatBotCommand.CHAT_GPT_HELP)) {
            answerHelp(bot, ChatBotCommand.CHAT_GPT_HELP, chatId);
        } else if (text.startsWith(ChatBotCommand.CHAT_GPT)) {
            //中断操作
            if (!config.isInWhiteList(chatId)) {
                answerHelp(bot, ChatBotCommand.CHAT_GPT_REGISTER, chatId);
                return;
            }
            content = text.substring(ChatBotCommand.CHAT_GPT.length());
            answerByChatGptAsync(bot, content, chatId);
        }

    }

    /**
     * 显示帮助信息
     *
     * @param bot      botApi
     * @param helpType 帮助类型
     * @param chatId   对话Id
     */
    private void answerHelp(TelegramBot bot, String helpType, Long chatId) {
        logUtils.log(LogUtils.LogLevel.INFO, config, "answerHelp", chatId, "helpType = " + helpType, null);
        String answer = "";
        switch (helpType) {
            case ChatBotCommand.START:
                answer = "欢迎使用Arno的Robot, 你可以输入 /help 查看帮助";
                break;
            case ChatBotCommand.HELP:
                /**
                 * chatgpt - 机器人聊天
                 * chatgpt_help - 机器人聊天帮助
                 * help - 帮助
                 */
                answer = "目前支持的命令有: \n" +
                        "/start 开始\n" +
                        "/help 帮助\n" +
                        "/chatgpt 机器人聊天\n" +
                        "/chatgpt_help 机器人聊天帮助\n";
                break;
            case ChatBotCommand.CHAT_GPT_HELP:
                answer = ChatBotCommand.CHAT_GPT_HELP_INFO;
                break;
            case ChatBotCommand.CHAT_GPT_REGISTER:
                logUtils.log(LogUtils.LogLevel.WARN, config, "answerHelp", chatId, "chatId = " + chatId + " 未注册", null);
                answer = "白名单机制开启,请向管理员Arno 反馈本次会话 id : \n" + chatId;
                break;
            default:
                answer = "未知命令";
                break;
        }
        SendResponse execute = bot.execute(new SendMessage(chatId, answer));
        boolean ok = execute.isOk();
        logUtils.log(LogUtils.LogLevel.INFO, config, "answerHelp", chatId, "answerHelp is send ok = " + ok, null);
    }

    /**
     * 异步chatgpt聊天
     *
     * @param bot      botApi
     * @param question 问题
     * @param chatId   对话Id
     */
    private void answerByChatGptAsync(TelegramBot bot, String question, Long chatId) {
        logUtils.log(LogUtils.LogLevel.INFO, config, "answerByChatGptAsync", chatId, "question = " + question, null);
        sendState(chatId, ChatAction.typing);
        if (StringUtils.isEmpty(question)) {
            bot.execute(new SendMessage(chatId, "请输入内容"));
            return;
        }
        chatService.doChat(question, answer -> {
            SendMessage sendMessage = new SendMessage(chatId, answer);
            bot.execute(sendMessage, new Callback<SendMessage, SendResponse>() {
                @Override
                public void onResponse(SendMessage request, SendResponse response) {
                    if (response.isOk()) {
                        logUtils.log(LogUtils.LogLevel.INFO, config, "answerByChatGptAsync", chatId, "answerByChatGptAsync send message ok", null);
                    } else {
                        logUtils.log(LogUtils.LogLevel.ERROR, config, "answerByChatGptAsync", chatId, "answerByChatGptAsync send message error, code = " + response.errorCode() + ", description = " + response.description(), null);
                    }
                }

                @Override
                public void onFailure(SendMessage request, IOException e) {
                    logUtils.log(LogUtils.LogLevel.ERROR, config, "answerByChatGptAsync", chatId, "answerByChatGptAsync send message error", e);
                }
            });
        });

    }


    /**
     * 状态更新
     *
     * @param chatId     对话Id
     * @param chatAction 状态
     */
    private void sendState(Long chatId, ChatAction chatAction) {
        SendChatAction sendChatAction = new SendChatAction(chatId, chatAction);
        bot.execute(sendChatAction);
    }


}
