package com.arno.tech.spring.telegram;

import com.arno.tech.spring.chatgpt.service.ChatService;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TelegramManger {
    private final ChatService chatService;
    private final TgConfig config;
    private TelegramBot bot;

    @Autowired
    public TelegramManger(ChatService chatService, TgConfig config) {
        this.chatService = chatService;
        this.config = config;
    }

    public void init() {
        bot = new TelegramBot(config.getToken());
        //调试用
//        bot = new TelegramBot.Builder(config.getToken()).debug().build();
        log.info("Telegram bot init name = {}, token = {}", config.getName(), config.getToken());

        //注册消息监听
        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                log.info("update = {}", update.message().text());
                dispatchUpdate(update);
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }

    /**
     * 分发消息
     *
     * @param update
     */
    private void dispatchUpdate(Update update) {
        if (update == null) {
            log.error("update is null");
            return;
        }
        if (update.message() == null) {
            log.error("update message is null");
            return;
        }
        if (update.message().text() == null) {
            log.error("update message text is null");
            return;
        }
        Long chatId = update.message().chat().id();
        String text = update.message().text();
        log.info("dispatchUpdate chatId = {}, text = {}", chatId, text);
        String content;
        if (text.startsWith(ChatBotCommand.START)) {
            answerHelp(bot, ChatBotCommand.START, chatId);
        } else if (text.startsWith(ChatBotCommand.HELP)) {
            answerHelp(bot, ChatBotCommand.HELP, chatId);
        } else if (text.startsWith(ChatBotCommand.CHAT_GPT_HELP)) {
            answerHelp(bot, ChatBotCommand.CHAT_GPT_HELP, chatId);
        } else if (text.startsWith(ChatBotCommand.CHAT_GPT)) {
            content = text.substring(ChatBotCommand.CHAT_GPT.length());
            answerByChatGptAsync(bot, content, chatId);
        }

    }

    /**
     * 显示帮助信息
     *
     * @param bot
     * @param helpType
     * @param chatId
     */
    private void answerHelp(TelegramBot bot, String helpType, Long chatId) {
        log.info("answerHelp chatId = {}, helpType = {}", chatId, helpType);
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
            default:
                answer = "未知命令";
                break;
        }
        SendResponse execute = bot.execute(new SendMessage(chatId, answer));
        boolean ok = execute.isOk();
        System.out.println("answerHelp is send ok = " + ok);
    }

    private void answerByChatGptAsync(TelegramBot bot, String question, Long chatId) {
        log.info("answerByChatGptAsync chatId = {}, question = {}", chatId, question);
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
                        log.info("answerByChatGptAsync send message ok");
                    } else {
                        log.error("answerByChatGptAsync send message error, code = {}, description = {}", response.errorCode(), response.description());
                    }
                }

                @Override
                public void onFailure(SendMessage request, IOException e) {
                    log.error("answerByChatGptAsync send message error", e);
                }
            });
        });

    }


    /**
     * 状态更新
     *
     * @param chatId
     * @param chatAction
     */
    private void sendState(Long chatId, ChatAction chatAction) {
        SendChatAction sendChatAction = new SendChatAction(chatId, chatAction);
        bot.execute(sendChatAction);
    }


}
