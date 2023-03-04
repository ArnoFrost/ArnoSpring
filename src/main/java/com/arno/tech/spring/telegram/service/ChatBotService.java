package com.arno.tech.spring.telegram.service;

import com.arno.tech.spring.chatgpt.ai.model.chat.ChatModelResponse;
import com.arno.tech.spring.chatgpt.ai.vo.ChatVo;
import com.arno.tech.spring.chatgpt.service.ChatService;
import com.arno.tech.spring.telegram.config.ChatBotCommand;
import com.arno.tech.spring.telegram.config.HelpInfo;
import com.arno.tech.spring.telegram.config.TgConfig;
import com.arno.tech.spring.telegram.model.IChatCacheModel;
import com.arno.tech.spring.telegram.model.bean.Chat;
import com.arno.tech.spring.telegram.model.bean.Role;
import com.arno.tech.spring.base.utils.LogUtils;
import com.arno.tech.spring.telegram.utils.TgApiUtils;
import com.arno.tech.spring.user.model.bean.User;
import com.arno.tech.spring.user.service.IUserInfoService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 聊天服务实现
 *
 * @author ArnoFrost
 * @since 2023/03/04
 */
@Service
public class ChatBotService implements IChatBotService {
    private final TgConfig config;
    private final IChatCacheModel chatCacheModel;
    private final ChatService chatService;
    private TelegramBot bot;
    private final IUserInfoService userInfoService;
    private final HelpInfo helpInfo;

    private final LogUtils logUtils;

    public ChatBotService(TgConfig config, IChatCacheModel chatCacheModel, ChatService chatService
            , IUserInfoService userInfoService, HelpInfo helpInfo) {
        this.config = config;
        this.chatCacheModel = chatCacheModel;
        this.chatService = chatService;
        this.userInfoService = userInfoService;
        this.helpInfo = helpInfo;
        this.logUtils = LogUtils.getInstance();
    }


    @Override
    public void init() {
        if (config.isDebug()) {
            bot = new TelegramBot.Builder(config.getToken()).debug().build();
            //调试用
            logUtils.log(LogUtils.LogLevel.DEBUG, "ChatBotService", null, "Telegram chat bot init name = " + config.getName() + ", token = " + config.getToken());
        } else {
            bot = new TelegramBot(config.getToken());
        }

        //注册消息监听
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::dispatchUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    @Override
    public boolean pushSingle(Long chatId, String msg) {
        if (chatId == null || StringUtils.isEmpty(msg)) {
            return false;
        }
        SendResponse execute = bot.execute(new SendMessage(chatId, msg));
        return execute.isOk();
    }

    @Override
    public boolean pushAll(String testMsg) {
        if (StringUtils.isEmpty(testMsg)) {
            return false;
        }
        List<User> userList = userInfoService.getUserList();
        if (userList == null || userList.isEmpty()) {
            return false;
        }
        for (User user : userList) {
            if (user == null) {
                continue;
            }
            Long chatId = user.getId();
            if (chatId == null) {
                continue;
            }
            pushSingle(chatId, testMsg);
        }
        return true;
    }

    /**
     * 分发消息
     *
     * @param update {@link Update}
     */
    private void dispatchUpdate(Update update) {
        if (update == null) {
            logUtils.log(LogUtils.LogLevel.ERROR, "dispatchUpdate", null, "update is null");
            return;
        }
        if (update.message() == null) {
            logUtils.log(LogUtils.LogLevel.ERROR, "dispatchUpdate", null, "update message is null");
            return;
        }
        if (update.message().text() == null) {
            logUtils.log(LogUtils.LogLevel.ERROR, "dispatchUpdate", null, "update message text is null");
            return;
        }
        Long chatId = update.message().chat().id();
        Integer messageId = update.message().messageId();
        String text = update.message().text();
        logUtils.log(LogUtils.LogLevel.INFO, "dispatchUpdate", chatId, "text = " + text);
        String content;
        if (text.startsWith(ChatBotCommand.START)) {
            answerHelp(ChatBotCommand.START, chatId);
        } else if (text.startsWith(ChatBotCommand.HELP)) {
            answerHelp(ChatBotCommand.HELP, chatId);
        } else if (text.startsWith(ChatBotCommand.CHAT_TEXT_HELP)) {
            answerHelp(ChatBotCommand.CHAT_TEXT_HELP, chatId);
        } else if (text.startsWith(ChatBotCommand.CHAT_TEXT)) {
            //中断操作
            if (!isValidUser(chatId)) {
                answerHelp(ChatBotCommand.REGISTER, chatId);
                logUtils.log(LogUtils.LogLevel.WARN, "dispatchUpdate", chatId, "chatId is not in white list");
                return;
            }
            content = text.substring(ChatBotCommand.CHAT_TEXT.length());
            answerByTextAsync(bot, content, chatId, messageId);
        } else if (text.startsWith(ChatBotCommand.CHAT_GPT_HELP)) {
            answerHelp(ChatBotCommand.CHAT_GPT_HELP, chatId);
        } else if (text.startsWith(ChatBotCommand.CHAT_GPT_CLEAR)) {
            //中断操作
            if (!isValidUser(chatId)) {
                answerHelp(ChatBotCommand.REGISTER, chatId);
                logUtils.log(LogUtils.LogLevel.WARN, "dispatchUpdate", chatId, "chatId is not in white list");
                return;
            }
            answerClear(bot, chatId);
        } else if (text.startsWith(ChatBotCommand.CHAT_GPT)) {
            //中断操作
            if (!isValidUser(chatId)) {
                answerHelp(ChatBotCommand.REGISTER, chatId);
                logUtils.log(LogUtils.LogLevel.WARN, "dispatchUpdate", chatId, "chatId is not in white list");
                return;
            }
            content = text.substring(ChatBotCommand.CHAT_GPT.length());
            answerByChatTurboAsync(bot, content, chatId, messageId);
        } else if (text.startsWith(ChatBotCommand.CHAT_GPT_SIMPLE)) {
            // 兜底改进用gpt 命令处理

            //中断操作
            if (!isValidUser(chatId)) {
                answerHelp(ChatBotCommand.REGISTER, chatId);
                logUtils.log(LogUtils.LogLevel.WARN, "dispatchUpdate", chatId, "chatId is not in white list");
                return;
            }
            content = text.substring(ChatBotCommand.CHAT_GPT_SIMPLE.length());
            answerByChatTurboAsync(bot, content, chatId, messageId);
        }

    }

    /**
     * 显示帮助信息
     *
     * @param helpType 帮助类型
     * @param chatId   对话Id
     */
    private void answerHelp(String helpType, Long chatId) {
        logUtils.log(LogUtils.LogLevel.INFO, "answerHelp", chatId, "helpType = " + helpType);
        String answer = "";
        switch (helpType) {
            case ChatBotCommand.START:
                answer = ChatBotCommand.INFO.START_INFO;
                break;
            case ChatBotCommand.HELP:
                answer = helpInfo.getHelpString();
                break;
            case ChatBotCommand.CHAT_GPT_HELP:
                answer = ChatBotCommand.INFO.CHAT_GPT_HELP_INFO;
                break;
            case ChatBotCommand.CHAT_TEXT_HELP:
                answer = ChatBotCommand.INFO.CHAT_TEXT_HELP_INFO;
                break;
            case ChatBotCommand.REGISTER:
                logUtils.log(LogUtils.LogLevel.WARN, "answerHelp", chatId, "chatId = " + chatId + " 未注册");
                answer = ChatBotCommand.INFO.REGISTER_INFO + chatId;
                break;
            default:
                answer = ChatBotCommand.INFO.UNKNOWN_INFO;
                break;
        }

        replyMessage("answerClear", chatId, null, answer);
    }

    /**
     * 清楚聊天记录
     *
     * @param bot    机器人
     * @param chatId 聊天id
     */
    private void answerClear(TelegramBot bot, Long chatId) {
        logUtils.log(LogUtils.LogLevel.INFO, "answerClear", chatId, "clear");
        boolean result = chatCacheModel.deleteChat(chatId.toString());
        String answer = ChatBotCommand.INFO.CLEAR_INFO_ERROR;
        if (result) {
            answer = ChatBotCommand.INFO.CLEAR_INFO_SUCCESS;
        }
        replyMessage("answerClear", chatId, null, answer);
    }

    /**
     * 异步text文本
     *
     * @param bot      botApi
     * @param question 问题
     * @param chatId   对话Id
     */
    private void answerByTextAsync(TelegramBot bot, String question, Long chatId, Integer messageId) {
        logUtils.log(LogUtils.LogLevel.INFO, "answerByTextAsync", chatId, "question = " + question + ",messageId = " + messageId);
        TgApiUtils.sendState(bot, chatId, ChatAction.typing);
        if (StringUtils.isEmpty(question)) {
            logUtils.log(LogUtils.LogLevel.ERROR, "answerByTextAsync", chatId, "question is null");
            bot.execute(new SendMessage(chatId, ChatBotCommand.INFO.QUESTION_IS_NULL));
            return;
        }
        chatService.doText(question, (answer, msg) -> {
            String answerMsg = answer != null ? answer : msg;
            replyMessage("answerByTextAsync", chatId, messageId, answerMsg);
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
        logUtils.log(LogUtils.LogLevel.INFO, "answerByChatTurboAsync", chatId, "question = " + question + ",messageId = " + messageId);
        TgApiUtils.sendState(bot, chatId, ChatAction.typing);
        if (StringUtils.isEmpty(question)) {
            logUtils.log(LogUtils.LogLevel.ERROR, "answerByChatTurboAsync", chatId, "question is null");
            bot.execute(new SendMessage(chatId, ChatBotCommand.INFO.QUESTION_IS_NULL));
            return;
        }
        //region 组装一条数据
        List<Chat> chat = chatCacheModel.getChat(chatId.toString());
        chat.add(new Chat(Role.ROLE_USER, question, System.currentTimeMillis()));
        //存入数据库
        chatCacheModel.addChat(chatId.toString(), Role.ROLE_USER, question);
        //endregion
        chatService.doChatByTurbo(chat, (chatVo, msg) -> {
            String answerMsg;
            if (chatVo != null) {
                //region 记录对话结果
                answerMsg = parseAnswer(chatVo);
                chatCacheModel.addChat(chatId.toString(), Role.ROLE_ASSISTANT, answerMsg);
                //endregion
            } else {
                answerMsg = msg;
            }
            replyMessage("answerByChatTurboAsync", chatId, messageId, answerMsg);
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

    private boolean isValidUser(Long chatId) {
        return userInfoService.isValidUser(chatId);
    }

    private void replyMessage(String tag, Long chatId, Integer messageId, String message) {
        TgApiUtils.replyMessage(tag, bot, logUtils, chatId, messageId, message);
    }
}
