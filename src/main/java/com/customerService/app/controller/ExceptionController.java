package com.customerService.app.controller;


import com.customerService.app.dto.ResponseDto;
import com.customerService.app.dto.ResponseException;
import com.customerService.app.dto.ResponseStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(Throwable.class)
    public ResponseDto generalHandler(Throwable t){
        return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Unexpected Error Has Occurred"));

    }
}
