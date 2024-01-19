package com.Acerise.System_api.Exception;

import com.Acerise.System_api.Exception.CommonException.CustomCommonBadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BadRequestCustomExceptionHandler {

    @ExceptionHandler(value = CustomCommonBadRequestException.class)
    public ResponseEntity<Object> EmailAlreadyInUseException(RuntimeException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);

    }
}

