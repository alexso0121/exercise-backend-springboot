package com.Acerise.System_api.Exception;

import com.Acerise.System_api.Exception.CommonException.CustomCommonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NotFoundExceptionHandler {
    @ExceptionHandler(value = CustomCommonNotFoundException.class)
    public ResponseEntity<Object> _NotFoundException(RuntimeException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);

    }
}
