package com.arno.tech.spring.telegram.service;

import com.arno.tech.spring.base.utils.LogUtils;
import com.arno.tech.spring.telegram.config.ChatBotCommand;
import com.arno.tech.spring.telegram.config.HelpInfo;
import com.arno.tech.spring.telegram.config.TgConfig;
import com.arno.tech.spring.telegram.utils.TgApiUtils;
import com.arno.tech.spring.user.model.bean.Role;
import com.arno.tech.spring.user.model.bean.User;
import com.arno.tech.spring.user.service.IUserInfoService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 聊天管理实现
 *
 * @author ArnoFrost
 * @since 2023/03/04
 */
@Service
public class ChatAdminBotService implements IChatAdminBotService {
    private final TgConfig config;

    private TelegramBot bot;

    private final IUserInfoService userInfoService;

    private final HelpInfo helpInfo;
    private final LogUtils logUtils;


    @Autowired
    public ChatAdminBotService(TgConfig config, IUserInfoService userInfoService, HelpInfo helpInfo) {
        this.config = config;
        this.userInfoService = userInfoService;
        this.helpInfo = helpInfo;
        logUtils = LogUtils.getInstance();
    }

    @Override
    public void init() {
        TgConfig.Admin admin = config.getAdmin();
        if (config.isDebug()) {
            bot = new TelegramBot.Builder(admin.getToken()).debug().build();
            //调试用
            logUtils.log(LogUtils.LogLevel.DEBUG, "ChatAdminBotService", null, "Telegram chat admin bot init name = " + admin.getName() + ", token = " + admin.getToken());
        } else {
            bot = new TelegramBot(admin.getToken());
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
        if (!userInfoService.isAdmin(chatId)) {
            replyMessage("dispatchUpdate", chatId, messageId, "你不是管理员，无法执行该操作~");
            return;
        }


        if (text.startsWith(ChatBotCommand.AdminCommand.REGISTER)) {
            String[] splitCommand = text.split("\\s+"); // 使用空格分割字符串
            String userId = splitCommand[1];
            String userName = splitCommand[2];
            doRegister(chatId, messageId, userId, userName);
        } else if (text.startsWith(ChatBotCommand.AdminCommand.DELETE_USER)) {
            String[] splitCommand = text.split("\\s+"); // 使用空格分割字符串
            String userId = splitCommand[1];
            doDeleteUser(chatId, messageId, userId);
        } else if (text.startsWith(ChatBotCommand.AdminCommand.HELP)) {
            replyMessage("dispatchUpdate", chatId, messageId, helpInfo.getAdminHelpString());
        } else if (text.startsWith(ChatBotCommand.AdminCommand.GET_USER_LIST)) {
            getUserList(chatId, messageId);
        } else {
            replyMessage("dispatchUpdate", chatId, messageId, "未知指令~");
        }

    }

    private void getUserList(Long chatId, Integer messageId) {
        List<User> userList = userInfoService.getUserList();
        if (userList == null || userList.isEmpty()) {
            replyMessage("getUserList", chatId, messageId, "用户列表为空~");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (User user : userList) {
            sb.append(user.toString()).append("\n");
        }

        replyMessage("getUserList", chatId, messageId, sb.toString());
    }


    private void doRegister(Long chatId, Integer messageId, String uid, String userName) {
        User user;
        try {
            user = new User();
            user.setId(Long.parseLong(uid));
            user.setName(userName);
            user.setRole(Role.USER);
            user.setStatus(1);
            boolean isAddSuccess = userInfoService.registerUser(user);
            if (isAddSuccess) {
                replyMessage("doRegister", chatId, messageId, "注册成功~");
            } else {
                replyMessage("doRegister", chatId, messageId, "注册失败");
            }
        } catch (NumberFormatException e) {
            replyMessage("doRegister", chatId, messageId, "注册失败，用户ID格式错误~");
        }
    }

    private void doDeleteUser(Long chatId, Integer messageId, String userId) {
        try {
            boolean isDeleted = userInfoService.deleteUser(Long.parseLong(userId));
            if (isDeleted) {
                replyMessage("doDeleteUser", chatId, messageId, "删除成功~");
            } else {
                replyMessage("doDeleteUser", chatId, messageId, "删除失败，用户不存在~");
            }
        } catch (NumberFormatException e) {
            replyMessage("doDeleteUser", chatId, messageId, "删除失败，用户ID格式错误~");
        }
    }


    private void replyMessage(String tag, Long chatId, Integer messageId, String message) {
        TgApiUtils.replyMessage(tag, bot, logUtils, chatId, messageId, message);
    }

}
