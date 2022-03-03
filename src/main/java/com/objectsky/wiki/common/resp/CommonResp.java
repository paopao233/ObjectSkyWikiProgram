package com.objectsky.wiki.common.resp;

import lombok.Data;

@Data
public class CommonResp<T> {

    /**
     * 业务上的成功或失败
     */
    private boolean success = true;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回泛型数据，自定义类型
     */
    private T data;

    /**
     * 返回成功结果  返回信息 和返回数据（任意格式）
     * @param message
     * @param data
     * @return
     * @Author lele
     */
    public static CommonResp success(String message, Object data) {
        CommonResp result = new CommonResp();
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 返回成功结果 但是固定了信息 只能传入一个object的data
     * @param data
     * @return
     * @Author lele
     */
    public static CommonResp success(Object data) {
        return success("success", data);
    }

    /**
     * 返回失败结果 返回信息 和返回数据（任意格式）
     * @param message
     * @param data
     * @return
     * @Author lele
     */
    public static CommonResp fail(String message, Object data) {
        CommonResp result = new CommonResp();
        result.setMessage(message);
        result.setData(data);
        return result;
    }
    /**
     * 返回失败结果 只能传入返回信息 默认data为null
     * @param message
     * @return
     * @Author lele
     */
    public static CommonResp fail(String message) {
        return fail(message, null);
    }

}
