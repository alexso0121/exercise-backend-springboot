package com.Acerise.System_api.Exception;

import com.Acerise.System_api.Exception.CommonException.CustomCommonInternalServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InternalServerException {
    @ExceptionHandler(value =CustomCommonInternalServerException.class)
    public ResponseEntity<Object> EmailAlreadyInUseException(RuntimeException exception){

        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
