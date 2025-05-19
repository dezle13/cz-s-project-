package com.example.bms.battery.common.exception;

import lombok.Getter;

/**
 * @author cz
 * @date 2025/5/18 13:13
 */

@Getter
public class DomainException extends RuntimeException{

    private final int code;
    private final String message;

    public DomainException(String msg){
        super(msg);
        this.code = 500;
        this.message = msg;
    }
}

