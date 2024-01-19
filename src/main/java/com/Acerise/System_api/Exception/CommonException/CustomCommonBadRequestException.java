package com.Acerise.System_api.Exception.CommonException;

public class CustomCommonBadRequestException extends  RuntimeException{
    public CustomCommonBadRequestException(String message) {
        super(message);
    }
    public CustomCommonBadRequestException(String message, Throwable cause) {
        super(message,cause);
    }
}

