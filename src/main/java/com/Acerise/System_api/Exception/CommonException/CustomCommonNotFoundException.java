package com.Acerise.System_api.Exception.CommonException;

public class CustomCommonNotFoundException extends  RuntimeException{
    public CustomCommonNotFoundException(String message) {
        super(message);
    }
    public CustomCommonNotFoundException(String message, Throwable cause) {
        super(message,cause);
    }
}
