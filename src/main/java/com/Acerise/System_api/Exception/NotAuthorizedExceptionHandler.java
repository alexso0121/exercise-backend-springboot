package com.Acerise.System_api.Exception;

import com.Acerise.System_api.Exception.CommonException.CustomCommonNotAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NotAuthorizedExceptionHandler {

    @ExceptionHandler(value= CustomCommonNotAuthorizedException.class)
    public ResponseEntity<Object> _NotActivatedException(RuntimeException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
