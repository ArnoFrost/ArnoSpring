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
import com.pengrad.telegrambot.model.request.ChatAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    private final IChatBotService chatBotService;

    private final HelpInfo helpInfo;
    private final LogUtils logUtils;


    @Autowired
    public ChatAdminBotService(TgConfig config, IUserInfoService userInfoService, ChatBotService chatBotService, HelpInfo helpInfo) {
        this.config = config;
        this.userInfoService = userInfoService;
        this.chatBotService = chatBotService;
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
        } else if (text.startsWith(ChatBotCommand.AdminCommand.PUSH_TEST)) {
            String pushMsg = text.substring(ChatBotCommand.AdminCommand.PUSH_TEST.length());
            pushTest(chatId, messageId, pushMsg);
        } else if (text.startsWith(ChatBotCommand.AdminCommand.PUSH_ALL)) {
            String pushMsg = text.substring(ChatBotCommand.AdminCommand.PUSH_ALL.length());
            pushAll(chatId, messageId, pushMsg);
        } else if (text.startsWith(ChatBotCommand.AdminCommand.FETCH_LOG)) {
            try {
                uploadLog(chatId);
            } catch (Exception e) {
                logUtils.log(LogUtils.LogLevel.ERROR, "dispatchUpdate", chatId, "upload log error", e);
                replyMessage("dispatchUpdate", chatId, messageId, "上传日志失败~");
            }
        } else {
            replyMessage("dispatchUpdate", chatId, messageId, "未知指令~");
        }

    }

    private void pushTest(Long chatId, Integer messageId, String pushMsg) {
        TgApiUtils.sendState(bot, chatId, ChatAction.typing);
        if (pushMsg == null || pushMsg.isEmpty()) {
            replyMessage("pushTest", chatId, messageId, "推送内容不能为空~");
            return;
        }
        String pushId = config.getAdmin().getId();
        boolean success = chatBotService.pushSingle(Long.parseLong(pushId), pushMsg);
        if (success) {
            replyMessage("pushTest", chatId, messageId, "推送成功~");
        } else {
            replyMessage("pushTest", chatId, messageId, "推送失败~");
        }
    }

    private void pushAll(Long chatId, Integer messageId, String pushMsg) {
        TgApiUtils.sendState(bot, chatId, ChatAction.typing);
        if (pushMsg == null || pushMsg.isEmpty()) {
            replyMessage("pushAll", chatId, messageId, "推送内容不能为空~");
            return;
        }
        //目前是阻塞形态，后续改成异步
        boolean success = chatBotService.pushAll(pushMsg);
        if (success) {
            replyMessage("pushAll", chatId, messageId, "推送成功~");
        } else {
            replyMessage("pushAll", chatId, messageId, "推送失败~");
        }
    }

    private void getUserList(Long chatId, Integer messageId) {
        List<User> userList = userInfoService.getUserList();
        if (userList == null || userList.isEmpty()) {
            replyMessage("getUserList", chatId, messageId, "用户列表为空~");
            return;
        }
        String result = userList.stream()
                .sorted(Comparator.comparing(User::getName))
                .map(user -> String.format("用户ID：%s，用户名：%s，状态：%s，角色：%s",
                        user.getId(), user.getName(), user.getStatus(), user.getRole()))
                .collect(Collectors.joining("\n"));

        replyMessage("getUserList", chatId, messageId, result);
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

    private void uploadLog(Long chatId) {
        String rootPath = "/Users/xuxin14/ServerGeek/ChatgptBot";
        File localDir = new File(rootPath + "/logs/local");
        File[] files = localDir.listFiles();
        List<File> fileList = Arrays.asList(files);
        List<File> collect = fileList.stream()
                .sorted(Comparator.comparingLong(File::lastModified).reversed())
                .limit(1)
                .collect(Collectors.toList());

        if (collect != null) {
            File file = collect.get(0);
            String fileName = file.getName();
            String filePath = file.getPath();
            logUtils.log(LogUtils.LogLevel.INFO, "uploadLog", chatId, "fileName = " + fileName);
            logUtils.log(LogUtils.LogLevel.INFO, "uploadLog", chatId, "filePath = " + filePath);
            TgApiUtils.sendState(bot, chatId, ChatAction.upload_document);
            TgApiUtils.sendDocument("uploadLog", bot, logUtils, chatId, filePath, fileName);
        }


    }

    private void replyMessage(String tag, Long chatId, Integer messageId, String message) {
        TgApiUtils.replyMessage(tag, bot, logUtils, chatId, messageId, message);
    }

}
