package com.arno.tech.spring.telegram;

import com.arno.tech.spring.chatgpt.ai.model.chat.ChatModelResponse;
import com.arno.tech.spring.chatgpt.ai.vo.ChatVo;
import com.arno.tech.spring.chatgpt.service.ChatService;
import com.arno.tech.spring.telegram.config.ChatBotCommand;
import com.arno.tech.spring.telegram.config.TgConfig;
import com.arno.tech.spring.telegram.model.IChatCacheModel;
import com.arno.tech.spring.telegram.model.bean.Chat;
import com.arno.tech.spring.telegram.model.bean.Role;
import com.arno.tech.spring.base.utils.LogUtils;
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
import java.util.List;

/**
 * Telegram 管理
 *
 * @author xuxin14
 * @since 2023/02/07
 */
@Component
public class TelegramBotManager {
    private final ChatService chatService;
    private final TgConfig config;
    private TelegramBot bot;

    private final LogUtils logUtils;

    private final IChatCacheModel chatCacheModel;

    @Autowired
    public TelegramBotManager(ChatService chatService, TgConfig config, IChatCacheModel chatCacheModel) {
        this.chatService = chatService;
        this.config = config;
        this.chatCacheModel = chatCacheModel;
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
        Integer messageId = update.message().messageId();
        String text = update.message().text();
        logUtils.log(LogUtils.LogLevel.INFO, config, "dispatchUpdate", chatId, "text = " + text, null);
        String content;
        if (text.startsWith(ChatBotCommand.START)) {
            answerHelp(bot, ChatBotCommand.START, chatId);
        } else if (text.startsWith(ChatBotCommand.HELP)) {
            answerHelp(bot, ChatBotCommand.HELP, chatId);
        } else if (text.startsWith(ChatBotCommand.CHAT_TEXT_HELP)) {
            answerHelp(bot, ChatBotCommand.CHAT_TEXT_HELP, chatId);
        } else if (text.startsWith(ChatBotCommand.CHAT_TEXT)) {
            //中断操作
            if (!config.isInWhiteList(chatId)) {
                answerHelp(bot, ChatBotCommand.REGISTER, chatId);
                logUtils.log(LogUtils.LogLevel.WARN, config, "dispatchUpdate", chatId, "chatId is not in white list", null);
                return;
            }
            content = text.substring(ChatBotCommand.CHAT_TEXT.length());
            answerByTextAsync(bot, content, chatId, messageId);
        } else if (text.startsWith(ChatBotCommand.CHAT_GPT_HELP)) {
            answerHelp(bot, ChatBotCommand.CHAT_GPT_HELP, chatId);
        } else if (text.startsWith(ChatBotCommand.CHAT_GPT_CLEAR)) {
            //中断操作
            if (!config.isInWhiteList(chatId)) {
                answerHelp(bot, ChatBotCommand.REGISTER, chatId);
                logUtils.log(LogUtils.LogLevel.WARN, config, "dispatchUpdate", chatId, "chatId is not in white list", null);
                return;
            }
            answerClear(bot, chatId);
        } else if (text.startsWith(ChatBotCommand.CHAT_GPT)) {
            //中断操作
            if (!config.isInWhiteList(chatId)) {
                answerHelp(bot, ChatBotCommand.REGISTER, chatId);
                logUtils.log(LogUtils.LogLevel.WARN, config, "dispatchUpdate", chatId, "chatId is not in white list", null);
                return;
            }
            content = text.substring(ChatBotCommand.CHAT_GPT.length());
            answerByChatTurboAsync(bot, content, chatId, messageId);
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
                answer = ChatBotCommand.INFO.CHAT_GPT_HELP_INFO;
                break;
            case ChatBotCommand.CHAT_TEXT_HELP:
                answer = ChatBotCommand.INFO.CHAT_TEXT_HELP_INFO;
                break;
            case ChatBotCommand.REGISTER:
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
     * 清楚聊天记录
     *
     * @param bot    机器人
     * @param chatId 聊天id
     */
    private void answerClear(TelegramBot bot, Long chatId) {
        logUtils.log(LogUtils.LogLevel.INFO, config, "answerClear", chatId, "clear", null);
        boolean result = chatCacheModel.deleteChat(chatId.toString());
        String answer = "删除失败，请重试";
        if (result) {
            answer = "已删除聊天记录，请开始新一轮对话\nHave fun~";
        }
        SendResponse execute = bot.execute(new SendMessage(chatId, answer));
        boolean ok = execute.isOk();
        logUtils.log(LogUtils.LogLevel.INFO, config, "answerClear", chatId, "answerClear is send ok = " + ok, null);
    }

    /**
     * 异步text文本
     *
     * @param bot      botApi
     * @param question 问题
     * @param chatId   对话Id
     */
    private void answerByTextAsync(TelegramBot bot, String question, Long chatId, Integer messageId) {
        logUtils.log(LogUtils.LogLevel.INFO, config, "answerByTextAsync", chatId, "question = " + question + ",messageId = " + messageId, null);
        sendState(chatId, ChatAction.typing);
        if (StringUtils.isEmpty(question)) {
            logUtils.log(LogUtils.LogLevel.ERROR, config, "answerByTextAsync", chatId, "question is null", null);
            bot.execute(new SendMessage(chatId, "请输入内容"));
            return;
        }
        chatService.doText(question, (answer, msg) -> {
            SendMessage sendMessage;
            if (answer != null) {
                sendMessage = new SendMessage(chatId, answer);
            } else {
                sendMessage = new SendMessage(chatId, msg);
            }
            sendMessage.replyToMessageId(messageId);
            bot.execute(sendMessage, new Callback<SendMessage, SendResponse>() {
                @Override
                public void onResponse(SendMessage request, SendResponse response) {
                    if (response.isOk()) {
                        logUtils.log(LogUtils.LogLevel.INFO, config, "answerByTextAsync", chatId, "send message ok", null);
                    } else {
                        logUtils.log(LogUtils.LogLevel.ERROR, config, "answerByTextAsync", chatId, "send message error, code = " + response.errorCode() + ", description = " + response.description(), null);
                    }
                }

                @Override
                public void onFailure(SendMessage request, IOException e) {
                    logUtils.log(LogUtils.LogLevel.ERROR, config, "answerByTextAsync", chatId, "send message error", e);
                }
            });
        });
    }

    /**
     * 异步text文本
     *
     * @param bot      botApi
     * @param question 问题
     * @param chatId   对话Id
     */
    private void answerByChatTurboAsync(TelegramBot bot, String question, Long chatId, Integer messageId) {
        logUtils.log(LogUtils.LogLevel.INFO, config, "answerByChatTurboAsync", chatId, "question = " + question + ",messageId = " + messageId, null);
        sendState(chatId, ChatAction.typing);
        if (StringUtils.isEmpty(question)) {
            logUtils.log(LogUtils.LogLevel.ERROR, config, "answerByChatTurboAsync", chatId, "question is null", null);
            bot.execute(new SendMessage(chatId, "请输入内容"));
            return;
        }
        //region 组装一条数据
        List<Chat> chat = chatCacheModel.getChat(chatId.toString());
        chat.add(new Chat(Role.ROLE_USER, question, System.currentTimeMillis()));
        //存入数据库
        chatCacheModel.addChat(chatId.toString(), Role.ROLE_USER, question);
        //endregion
        chatService.doChatByTurbo(chat, (chatVo, msg) -> {
            SendMessage sendMessage;
            if (chatVo != null) {
                //region 记录对话结果
                String answer = parseAnswer(chatVo);
                chatCacheModel.addChat(chatId.toString(), Role.ROLE_ASSISTANT, answer);
                //endregion
                sendMessage = new SendMessage(chatId, answer);
            } else {
                sendMessage = new SendMessage(chatId, msg);
            }
            sendMessage.replyToMessageId(messageId);
            bot.execute(sendMessage, new Callback<SendMessage, SendResponse>() {
                @Override
                public void onResponse(SendMessage request, SendResponse response) {
                    if (response.isOk()) {
                        logUtils.log(LogUtils.LogLevel.INFO, config, "answerByChatTurboAsync", chatId, "send message ok", null);
                    } else {
                        logUtils.log(LogUtils.LogLevel.ERROR, config, "answerByChatTurboAsync", chatId, "send message error, code = " + response.errorCode() + ", description = " + response.description(), null);
                    }
                }

                @Override
                public void onFailure(SendMessage request, IOException e) {
                    logUtils.log(LogUtils.LogLevel.ERROR, config, "answerByChatTurboAsync", chatId, "send message error", e);
                }
            });
        });
    }

    /**
     * 答案解析
     *
     * @param answer 回答
     * @return {@link String}
     */
    private String parseAnswer(ChatVo answer) {
        System.out.println(answer);
        if (answer == null) {
            return "未知错误";
        }
        StringBuilder sb = new StringBuilder();
        List<ChatModelResponse.Choice> choices = answer.getChoices();
        sb.append(choices.stream()
                        .skip(choices.size() - 1)
                        .limit(1)
                        .findFirst().get().getMessage().getContent())
                .append("\n");
        return sb.toString();
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
