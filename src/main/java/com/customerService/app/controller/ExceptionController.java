package com.customerService.app.controller;


import com.customerService.app.dto.ResponseDto;
import com.customerService.app.dto.ResponseException;
import com.customerService.app.dto.ResponseStatus;
import com.customerService.app.utility.AccountException;
import com.customerService.app.utility.LegalPersonException;
import com.customerService.app.utility.RealPersonException;
import com.customerService.app.utility.TransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ResponseDto> generalHandler(Throwable t) {
        String error;
        if (t instanceof HttpMessageNotReadableException)
            error = "JSON Parser Has Error Parsing Objects";
        if (t instanceof RealPersonException)
            error ="" + t.getMessage();
        if (t instanceof LegalPersonException)
            error = t.getMessage();
        if (t instanceof AccountException)
            error = t.getMessage();
        if (t instanceof TransactionException)
            error = t.getMessage();
        else
            error = "Unexpected Error Has Occurred";

        ResponseDto responseObject = new ResponseDto(ResponseStatus.Error, null, null, new ResponseException(error));
        return new ResponseEntity<>(responseObject, HttpStatus.OK);

    }
}
