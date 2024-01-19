package com.Acerise.System_api.Exception.CommonException;

public class CustomCommonInternalServerException extends RuntimeException{
    public CustomCommonInternalServerException(String message) {
        super(message);
    }
    public CustomCommonInternalServerException(String message, Throwable cause) {
        super(message,cause);
    }
}
