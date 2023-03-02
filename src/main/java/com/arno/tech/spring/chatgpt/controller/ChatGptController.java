package com.arno.tech.spring.chatgpt.controller;

import com.arno.tech.spring.base.pojo.Result;
import com.arno.tech.spring.base.utils.GlobalError;
import com.arno.tech.spring.chatgpt.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * chatgpt 服务
 *
 * @author xuxin14
 * @since 2023/02/02
 */
@RestController
@Slf4j
public class ChatGptController {

    private final ChatService chatService;

    @Autowired
    public ChatGptController(ChatService chatService) {
        this.chatService = chatService;
    }

//    @PostMapping("/chatgpt/chat")
//    public Result<String> chatGpt(@RequestParam Map<String, String> requestMapping) {
//        String question = requestMapping.get("question");
//        if (question == null || question.isEmpty()) {
//            return Result.error(GlobalError.PARAM_ERROR, "question is empty");
//        }
//        try {
//            String result = chatService.doText(question);
//            return Result.succ(result);
//        } catch (IOException e) {
//            return Result.error(GlobalError.SYS_ERROR, e.getMessage());
//        }
//    }

}
