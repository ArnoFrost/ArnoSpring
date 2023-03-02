package com.arno.tech.spring.chatgpt.ai.service;

import com.arno.tech.spring.chatgpt.ai.IOpenAI;
import com.arno.tech.spring.chatgpt.ai.config.ChatConfig;
import com.arno.tech.spring.chatgpt.ai.model.aggregates.AIAnswer;
import com.arno.tech.spring.chatgpt.ai.utils.OkHttpUtils;
import com.arno.tech.spring.chatgpt.config.mode.GptMode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


/**
 * OpenAI 请求管理
 *
 * @author xuxin14
 * @since 2023/02/07
 */
@Service
@Slf4j
public class OpenAIServiceImpl implements IOpenAI {
    private final OkHttpUtils okHttpUtils;
    private final TypeReference<AIAnswer> typeReference = new TypeReference<AIAnswer>() {
    };


    @Autowired
    public OpenAIServiceImpl(ChatConfig config) {
        //初始化okhttp
        OkHttpClient client = (new OkHttpClient.Builder())
                .connectionPool(new ConnectionPool(config.getMaxIdleConnections(), config.getKeepAliveDuration(), TimeUnit.SECONDS))
                .connectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(config.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(config.getWriteTimeout(), TimeUnit.SECONDS)
                .build();
        okHttpUtils = new OkHttpUtils(client);

    }

    @Override
    public String doChatGPT(String openAiKey, String question) {
        JsonObject params = new JsonObject();
        params.addProperty("model", "text-davinci-003");
        params.addProperty("prompt", question);
        params.addProperty("temperature", 0);
        params.addProperty("max_tokens", 1024);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + openAiKey);

        AIAnswer aiAnswer = okHttpUtils.doPostApi("doChatGpt", "https://api.openai.com/v1/completions", headers, params, typeReference);
        StringBuilder answers = new StringBuilder();
        List<AIAnswer.Choices> choices = aiAnswer.getChoices();
        for (AIAnswer.Choices choice : choices) {
            answers.append(choice.getText());
        }
        log.info("doChatGpt:aiAnswer{}", aiAnswer);
        return answers.toString();
    }

    @Override
    public void doChatGPT(String openAiKey, String question, @NotNull Consumer<String> consumer) {
        JsonObject params = new JsonObject();
        params.addProperty("model", "text-davinci-003");
        params.addProperty("prompt", question);
        params.addProperty("temperature", 0);
        params.addProperty("max_tokens", 1024);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + openAiKey);

        AIAnswer aiAnswer = okHttpUtils.doPostApi("doChatGpt", "https://api.openai.com/v1/completions", headers, params, typeReference);
        StringBuilder answers = new StringBuilder();
        if (aiAnswer == null) {
            consumer.accept("请求失败");
            return;
        }
        List<AIAnswer.Choices> choices = aiAnswer.getChoices();
        if (choices == null || choices.size() == 0) {
            consumer.accept("请求失败");
            return;
        }
        for (AIAnswer.Choices choice : choices) {
            answers.append(choice.getText());
        }
        log.info("doChatGpt:aiAnswer{}", aiAnswer);
        consumer.accept(answers.toString());
    }

    @Override
    public void doChatGPT(String openAiKey, GptMode mode, String question, Consumer<String> consumer) {
        JsonObject params = new JsonObject();
        params.addProperty("model", mode.getValue());
        params.addProperty("prompt", question);
        params.addProperty("temperature", 0);
        params.addProperty("max_tokens", 1024);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + openAiKey);

        String url = "https://api.openai.com/v1/completions";
        switch (mode) {
            case DAVINCI_3:
                url = "https://api.openai.com/v1/engines/davinci/completions";
                break;
            case CHAT_TURBO:
                url = "https://api.openai.com//v1/chat/completions";
                break;
            default:
                break;
        }
        AIAnswer aiAnswer = okHttpUtils.doPostApi("doChatGpt", url, headers, params, typeReference);
        StringBuilder answers = new StringBuilder();
        if (aiAnswer == null) {
            consumer.accept("请求失败");
            return;
        }
        List<AIAnswer.Choices> choices = aiAnswer.getChoices();
        if (choices == null || choices.size() == 0) {
            consumer.accept("请求失败");
            return;
        }
        for (AIAnswer.Choices choice : choices) {
            answers.append(choice.getText());
        }
        log.info("doChatGpt:aiAnswer{}", aiAnswer);
        consumer.accept(answers.toString());
    }

}
