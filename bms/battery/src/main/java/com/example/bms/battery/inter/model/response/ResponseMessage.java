package com.example.bms.battery.inter.model.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author cz
 * @date 2025/5/18 11:30
 */
@Data
public class ResponseMessage<T> {
    private Integer code;
    private String message;
    private T data;

    public ResponseMessage(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseMessage<T> success(T data) {
        return new ResponseMessage<T>(HttpStatus.OK.value(), "ok", data);
    }

    public static <T> ResponseMessage<T> success() {
        return new ResponseMessage<T>(HttpStatus.OK.value(), "ok", null);
    }

    public static <T> ResponseMessage<T> error(String msg) {
        return new ResponseMessage<T>(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, null);
    }

    public static <T> ResponseMessage<T> requestError(String msg) {
        return new ResponseMessage<T>(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, null);
    }


}
