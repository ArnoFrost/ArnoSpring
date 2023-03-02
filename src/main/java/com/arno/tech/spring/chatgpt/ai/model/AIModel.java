package com.arno.tech.spring.chatgpt.ai.model;

import com.arno.tech.spring.chatgpt.ai.model.aggregates.AIAnswer;
import com.arno.tech.spring.chatgpt.ai.model.chat.ChatModelResponse;
import com.arno.tech.spring.chatgpt.ai.utils.OkHttpUtils;
import com.arno.tech.spring.chatgpt.config.constant.OpenAIUrl;
import com.arno.tech.spring.chatgpt.config.mode.GptMode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * AI数据管理层
 *
 * @author xuxin14
 * @since 2023/03/02
 */
@Repository
public class AIModel implements IAIModel {

    private final OkHttpUtils okHttpUtils;
    private final TypeReference<AIAnswer> aiAnswerTypeReference = new TypeReference<AIAnswer>() {
    };
    private final TypeReference<ChatModelResponse> chatReference = new TypeReference<ChatModelResponse>() {
    };

    @Autowired
    public AIModel(OkHttpClient client) {
        this.okHttpUtils = new OkHttpUtils(client);
    }

    @Override
    public void doText3(String openAiKey, String question, Consumer<AIAnswer> aiAnswerConsumer) {
        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("model", "text-davinci-003");
        requestJson.addProperty("prompt", question);
        requestJson.addProperty("temperature", 0);
        requestJson.addProperty("max_tokens", 1024);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + openAiKey);
        requestApi("doText3", openAiKey, OpenAIUrl.URL_TEXT_3, requestJson, aiAnswerTypeReference, aiAnswerConsumer);
    }

    @Override
    public void doChatByTurbo(String openAiKey, String question, Consumer<ChatModelResponse> consumer) {
        JsonObject params = new JsonObject();
        params.addProperty("model", GptMode.CHAT_TURBO.getValue());
        JsonArray message = new JsonArray();
        message.add(getUserMessage(question));
        params.addProperty("temperature", 0);
        params.addProperty("max_tokens", 1024);
        requestApi("doChatByTurbo", openAiKey, OpenAIUrl.URL_CHAT_TURBO, params, chatReference, consumer);
    }


    //region 私有逻辑
    private <T> void requestApi(String tag, String openAiKey, @NotNull String url, JsonObject requestJson, TypeReference typeReference, Consumer<T> consumer) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + openAiKey);
        okHttpUtils.doPostApi(tag, url, headers, requestJson, typeReference, consumer);
    }

    private static JsonObject getUserMessage(String question) {
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("content", question);
        userMessage.addProperty("role", "user");
        return userMessage;
    }
    //endregion
}
