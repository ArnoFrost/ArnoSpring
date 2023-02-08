package com.arno.tech.spring.chatgpt.ai.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 网络请求工具类
 * 初步封装采用需要实例化的方式
 *
 * @author xuxin14
 * @since 2022/12/13
 */
@Slf4j
public class OkHttpUtils {
    /**
     * 请求客户端
     */
    private final OkHttpClient client;

    /**
     * 对象映射器
     */
    private final ObjectMapper objectMapper;

    /**
     * 网络请求工具类
     *
     * @param client 依赖的OkHttpClient
     */
    public OkHttpUtils(OkHttpClient client) {
        this.client = client;
        this.objectMapper = new ObjectMapper();
    }


    public <T> T doPostApiWithFile(String tag, String mediaType, String fileTag, byte[] file, String url,
                                   Map<String, String> headers, Map<String, String> requestData, TypeReference<T> typeReference) {
        //region 构建请求
        MediaType type = MediaType.parse(mediaType);
        RequestBody fileBody = RequestBody.create(type, file);
        // MultipartBody 上传文件专用的请求体
        MultipartBody.Builder formBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(fileTag, fileTag, fileBody);
        //添加其他参数
        if (requestData != null && !requestData.isEmpty()) {
            for (String key : requestData.keySet()) {
                if (key != null && requestData.get(key) != null) {
                    formBodyBuilder.addFormDataPart(key, requestData.get(key));
                }
            }
        }
        //构建请求体
        Request.Builder requestBuilder = new Request.Builder().url(url)
                .post(formBodyBuilder.build());
        //追加请求头
        if (headers != null && !headers.isEmpty()) {
            for (String key : headers.keySet()) {
                if (key != null && headers.get(key) != null) {
                    requestBuilder.addHeader(key, headers.get(key));
                }
            }
        }
        RequestBody body = formBodyBuilder.build();
        Request request = requestBuilder
                .url(url)
                .post(body)
                .build();
        //endregion

        //region 请求解析
        try {
            String resp;
            try (Response response = client.newCall(request).execute()) {
                if (response.body() == null) {
                    log.error(tag + " doPostApiWithFile response body is null");
                    return null;
                }
                if (!response.isSuccessful()) {
                    log.error(tag + " doPostApiWithFile response is not successful");
                    return null;
                }
                resp = response.body().string();
            }
            if (log.isInfoEnabled()) {
                log.info(tag + " doPostApiWithFile response: {}", resp);
            }
            return objectMapper.readValue(resp, typeReference);
        } catch (Exception e) {
            log.error(tag + " doPostApiWithFile error {}", e);
        }
        //endregion
        return null;
    }

    /**
     * 同步post网络请求封装
     *
     * @param tag         标签
     * @param url         url
     * @param headers     请求头
     * @param requestData 请求数据
     * @return {@link T}
     */
    public <T> T doPostApi(String tag, @NotNull String url, Map<String, String> headers, Map<String, String> requestData, TypeReference<T> typeReference) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
        try {
            //region //构建请求体
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            if (requestData != null && !requestData.isEmpty()) {
                //添加其他参数
                for (String key : requestData.keySet()) {
                    if (key != null && requestData.get(key) != null) {
                        formBodyBuilder.add(key, requestData.get(key));
                    }
                }
            }
            Request.Builder requestBuilder = new Request.Builder().url(uriComponentsBuilder.toUriString())
                    .post(formBodyBuilder.build());
            //追加请求头
            if (headers != null && !headers.isEmpty()) {
                for (String key : headers.keySet()) {
                    if (key != null && headers.get(key) != null) {
                        requestBuilder.addHeader(key, headers.get(key));
                    }
                }
            }
            Request request = requestBuilder.build();
            //endregion


            //region 请求解析
            String resp;
            try (Response response = client.newCall(request).execute()) {
                if (response.body() == null) {
                    log.error(tag + " doPostApi response body is null");
                    return null;
                }
                if (!response.isSuccessful()) {
                    log.error(tag + " doPostApi response is not successful");
                    return null;
                }
                resp = response.body().string();
            }
            if (log.isInfoEnabled()) {
                log.info(tag + " doPostApi response: {}", resp);
            }
            return objectMapper.readValue(resp, typeReference);
            //endregion
        } catch (Exception e) {
            log.error(tag + " doPostApi error {}", e);
        }
        return null;
    }

    /**
     * 同步post网络请求封装
     *
     * @param tag         标签
     * @param url         url
     * @param headers     请求头
     * @param requestJson 请求数据
     * @return {@link T}
     */
    public <T> T doPostApi(String tag, @NotNull String url, Map<String, String> headers, JsonObject requestJson, TypeReference<T> typeReference) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
        try {
            //region //构建请求体
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, requestJson.toString());
            Request.Builder requestBuilder = new Request.Builder().url(uriComponentsBuilder.toUriString())
                    .post(body);
            //追加请求头
            if (headers != null && !headers.isEmpty()) {
                for (String key : headers.keySet()) {
                    if (key != null && headers.get(key) != null) {
                        requestBuilder.addHeader(key, headers.get(key));
                    }
                }
            }
            Request request = requestBuilder.build();
            //endregion


            //region 请求解析
            String resp;
            try (Response response = client.newCall(request).execute()) {
                if (response.body() == null) {
                    log.error(tag + " doPostApi response body is null");
                    return null;
                }
                if (!response.isSuccessful()) {
                    log.error(tag + " doPostApi response is not successful response code: {}", response.code());
                    return null;
                }
                resp = response.body().string();
            }
            if (log.isInfoEnabled()) {
                log.info(tag + " doPostApi response: {}", resp);
            }
            return objectMapper.readValue(resp, typeReference);
            //endregion
        } catch (Exception e) {
            log.error(tag + " doPostApi error {}", e);
        }
        return null;
    }

    /**
     * 异步构建请求
     *
     * @param tag
     * @param url
     * @param headers
     * @param requestJson
     * @param typeReference
     * @param consumer
     */
    public <T> void doPostApi(String tag, @NotNull String url, Map<String, String> headers, JsonObject requestJson, TypeReference<T> typeReference, @NotNull Consumer<T> consumer) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
        try {
            //region //构建请求体
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, requestJson.toString());
            Request.Builder requestBuilder = new Request.Builder().url(uriComponentsBuilder.toUriString())
                    .post(body);
            //追加请求头
            if (headers != null && !headers.isEmpty()) {
                for (String key : headers.keySet()) {
                    if (key != null && headers.get(key) != null) {
                        requestBuilder.addHeader(key, headers.get(key));
                    }
                }
            }
            Request request = requestBuilder.build();
            //endregion
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    log.error(tag + " doPostApi error {}", e);
                    consumer.accept(null);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    //region 请求解析
                    String resp;
                    if (response.body() == null) {
                        log.error(tag + " doPostApi response body is null");
                        consumer.accept(null);
                    }
                    if (!response.isSuccessful()) {
                        log.error(tag + " doPostApi response is not successful");
                        consumer.accept(null);
                    }
                    resp = response.body().string();
                    if (log.isInfoEnabled()) {
                        log.info(tag + " doPostApi response: {}", resp);
                    }
                    consumer.accept(objectMapper.readValue(resp, typeReference));
                    //endregion
                }
            });
        } catch (Exception e) {
            log.error(tag + " doPostApi error {}", e);
        }
    }
}
