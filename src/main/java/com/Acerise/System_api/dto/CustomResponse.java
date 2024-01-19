package com.Acerise.System_api.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Data
@Builder
public class CustomResponse<T> {
    private T body;
    private HttpStatus status;
    private String message;
    private LocalDateTime dateTime;


    @Builder
    public CustomResponse(T body, HttpStatus status, String message, LocalDateTime dateTime) {
        this.body = body;
        this.status = status;
        this.message = message;
        this.dateTime = dateTime;
    }



}
