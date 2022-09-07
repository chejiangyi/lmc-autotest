package com.lmc.autotest.core;

/**
 * @author: chejiangyi
 * @since: 2020/1/10 9:17
 * @description: 响应数据返回码接口，只定义了处理成功和失败两个返回码。扩张可以进行继承
 */
public interface ResponseCode {
    /**
     * 处理成功
     */
    ResponseStatus SUCCESS = new ResponseStatus(200, "处理成功");
    /**
     * 处理失败
     */
    ResponseStatus FAIL = new ResponseStatus(500, "处理失败");

    /****---------------系统异常或错误----------------------------****/
    ResponseStatus ERROR_400 = new ResponseStatus(400, "请求参数错误");
    ResponseStatus ERROR_500 = new ResponseStatus(500, "系统处理异常，请稍后再试");
/****---------------系统异常或错误---------------end-------------****/

    /**
     * 返回状态内部类
     */
    class ResponseStatus {
        public ResponseStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
        /**
         * 返回码
         */
        private int code;
        /**
         * 返回消息
         */
        private String msg;

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }


}
