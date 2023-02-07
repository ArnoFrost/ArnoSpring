package com.arno.tech.spring.base.pojo;


import com.arno.tech.spring.base.utils.GlobalError;
import lombok.Data;

/**
 * 统一的结果类
 *
 * @author suanxiao5
 */
@Data
public class Result<T> {
    private int status;
    private String msg;
    private T data;

    private Result(T data) {
        this.status = GlobalError.SUCC;
        this.msg = "success";
        this.data = data;
    }

    private Result(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    /**
     * 错误但是包含数据的情况
     *
     * @param status
     * @param msg
     * @param data
     */
    private Result(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> succ(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> error(int status, String msg) {
        return new Result<>(status, msg);
    }

    /**
     * 错误但是包含数据的情况
     *
     * @param status
     * @param msg
     * @param data
     * @return {@link Result}<{@link T}>
     */
    public static <T> Result<T> error(int status, String msg, T data) {
        return new Result<>(status, msg, data);
    }

}

