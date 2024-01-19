package com.Acerise.System_api.Exception.CommonException;

public class CustomCommonNotAuthorizedException extends RuntimeException {
    public CustomCommonNotAuthorizedException(String message) {
        super(message);
    }
    public CustomCommonNotAuthorizedException(String message, Throwable cause) {
        super(message,cause);
    }

}
