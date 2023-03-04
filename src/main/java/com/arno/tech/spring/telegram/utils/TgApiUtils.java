package com.arno.tech.spring.telegram.utils;

import com.arno.tech.spring.base.utils.LogUtils;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;

public class TgApiUtils {
    /**
     * 状态更新
     *
     * @param bot        botApi
     * @param chatId     对话Id
     * @param chatAction 状态
     */
    public static void sendState(@NotNull TelegramBot bot, Long chatId, ChatAction chatAction) {
        SendChatAction sendChatAction = new SendChatAction(chatId, chatAction);
        bot.execute(sendChatAction);
    }

    public static void replyMessage(String tag, @NotNull TelegramBot bot, LogUtils logUtils, Long chatId, Integer messageId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        if (messageId != null) {
            sendMessage.replyToMessageId(messageId);
        }
        bot.execute(sendMessage, new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage request, SendResponse response) {
                if (response.isOk()) {
                    logUtils.log(LogUtils.LogLevel.INFO, tag, chatId, "send message ok");
                } else {
                    logUtils.log(LogUtils.LogLevel.ERROR, tag, chatId, "send message error, code = " + response.errorCode() + ", description = " + response.description());
                }
            }

            @Override
            public void onFailure(SendMessage request, IOException e) {
                logUtils.log(LogUtils.LogLevel.ERROR, tag, chatId, "send message error", e);
            }
        });
    }

    public static void replyMessage(String tag, @NotNull TelegramBot bot, LogUtils logUtils, Long chatId, SendMessage msg) {
        bot.execute(msg, new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage request, SendResponse response) {
                if (response.isOk()) {
                    logUtils.log(LogUtils.LogLevel.INFO, tag, chatId, "send message ok");
                } else {
                    logUtils.log(LogUtils.LogLevel.ERROR, tag, chatId, "send message error, code = " + response.errorCode() + ", description = " + response.description());
                }
            }

            @Override
            public void onFailure(SendMessage request, IOException e) {
                logUtils.log(LogUtils.LogLevel.ERROR, tag, chatId, "send message error", e);
            }
        });
    }


    public static void sendDocument(String tag, @NotNull TelegramBot bot, LogUtils logUtils, Long chatId, String filePath, String fileName) {
        bot.execute(new SendDocument(chatId, new File(filePath))
                .fileName(fileName), new Callback<SendDocument, SendResponse>() {
            @Override
            public void onResponse(SendDocument request, SendResponse response) {
                if (response.isOk()) {
                    logUtils.log(LogUtils.LogLevel.INFO, tag, chatId, "send document ok");
                } else {
                    logUtils.log(LogUtils.LogLevel.ERROR, tag, chatId, "send document error, code = " + response.errorCode() + ", description = " + response.description());
                }
            }

            @Override
            public void onFailure(SendDocument request, IOException e) {
                logUtils.log(LogUtils.LogLevel.ERROR, tag, chatId, "send document error", e);
            }
        });
    }
}
