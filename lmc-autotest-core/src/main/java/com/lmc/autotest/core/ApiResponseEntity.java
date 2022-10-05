package com.lmc.autotest.core;

import com.free.bsf.core.util.StringUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author: chejiangyi
 * @date: 2020/4/20 21:38
 * @version: v1.3.0
 * @desc: 微服务调用统一返回包装器对象，1.支持泛型；2.data可为基本数据类型或null,不进行二次转换包装
 */
@Data
public class ApiResponseEntity<T> {
    /**
     * 返回值
     */
    private int code;

    /**
     * 返回说明
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * debug信息
     */
    private Map<String,Object> debug=new HashMap<>();

    public ApiResponseEntity(int code, String message, T data){
        this.code=code;
        this.message=message;
        this.data=data;
    }

    public ApiResponseEntity(){
        //none
    }

    /**
     * 功能描述: 返回处理成功的响应对象
     * @return ApiResponseEntity
     */
    public static<T> ApiResponseEntity<T> success() {
        return success(null);
    }

    /**
     * 功能描述: 返回处理成功的响应对象
     * @param data -- 响应数据
     * @return ApiResponseEntity
     */
    public static<T> ApiResponseEntity<T> success(T data) {
        return success(null, data);
    }

    /**
     * 功能描述: 返回处理成功的响应对象
     * @param msg -- 响应消息
     * @param data -- 响应数据
     * @return ApiResponseEntity
     */
    public static<T> ApiResponseEntity<T> success(String msg, T data) {
        if (StringUtils.isEmpty(msg)) {
            msg = ResponseCode.SUCCESS.getMsg();
        }
        return new ApiResponseEntity<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 功能描述: 返回处理失败的响应消息
     * @return ApiResponseEntity
     */
    public static<T> ApiResponseEntity<T> fail() {
        return fail(null);
    }

    /**
     * 功能描述: 返回处理失败的响应消息
     * @param msg -- 响应消息
     * @return ApiResponseEntity
     */
    public static<T> ApiResponseEntity<T> fail(String msg) {
        return fail(msg, null);
    }

    /**
     * 功能描述: 返回处理失败的响应消息
     * @param msg -- 响应消息
     * @return ApiResponseEntity
     */
    public static<T> ApiResponseEntity<T> fail(int code, String msg) {
        if (StringUtils.isEmpty(msg)) {
            msg = ResponseCode.FAIL.getMsg();
        }
        return new ApiResponseEntity<T>(code, msg, null);
    }

    /**
     * 功能描述: 返回处理失败的响应消息
     * @param msg -- 响应消息
     * @param data -- 响应数据
     * @return ApiResponseEntity
     */
    public static<T> ApiResponseEntity<T> fail(String msg, T data) {
        if (StringUtils.isEmpty(msg)) {
            msg = ResponseCode.FAIL.getMsg();
        }
        return new ApiResponseEntity(ResponseCode.FAIL.getCode(), msg, data);
    }

    /**
     * 功能描述: 返回处理结果的响应对象
     * @param code 返回码
     * @param message 返回消息
     * @return 返回处理结果的响应对象
     */
    public static<T> ApiResponseEntity<T> of(int code, String message, T data) {
        return new ApiResponseEntity(code, message, data);
    }

    /**
     * 功能描述: 返回处理结果的响应对象
     * @param responseStatus -- 相应状态码和消息
     * @param data -- 返回的数据
     * @return ApiResponseEntity
     */
    public static<T> ApiResponseEntity<T> of(ResponseCode.ResponseStatus responseStatus, T data) {
        return new ApiResponseEntity(responseStatus.getCode(), responseStatus.getMsg(), data);
    }

    /**
     * 判断是否为成功的响应码
     * @param apiResponseEntity -- 响应对象
     * @return
     */
    public static boolean isSuccess(ApiResponseEntity apiResponseEntity) {
        return apiResponseEntity.getCode() == ResponseCode.SUCCESS.getCode()||apiResponseEntity.getCode() == 2000;//兼容旧版本协议2000
    }

//    /**
//     * 判断是否为失败，如果是失败，则抛出BusException异常
//     * 异常code和message都是ApiResponseEntity中的code和message
//     * @param apiResponseEntity -- 响应对象
//     * @return
//     */
//    public static void isFailAndThrowBizException(ApiResponseEntity apiResponseEntity) {
//        if (!isSuccess(apiResponseEntity)) {
//            throw new LmcBizException(new ResponseCode.ResponseStatus(apiResponseEntity.getCode(), apiResponseEntity.getMessage()));
//        }
//    }
}
