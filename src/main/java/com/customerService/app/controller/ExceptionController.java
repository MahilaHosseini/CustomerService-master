package com.customerService.app.controller;


import com.customerService.app.dto.ResponseDto;
import com.customerService.app.dto.ResponseException;
import com.customerService.app.dto.ResponseStatus;
import com.customerService.app.exception.AccountException;
import com.customerService.app.exception.LegalPersonException;
import com.customerService.app.exception.RealPersonException;
import com.customerService.app.exception.TransactionException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ResponseDto> generalHandler(Throwable t) {
        String error;
        if (t instanceof HttpMessageNotReadableException)
            error = environment.getProperty("customerService.exception.json");
        else if (t instanceof RealPersonException)
            error = environment.getProperty("customerService.exception.real") + t.getMessage();
        else if (t instanceof LegalPersonException)
            error = environment.getProperty("customerService.exception.legal") + t.getMessage();
        else if (t instanceof AccountException)
            error = environment.getProperty("customerService.exception.account") + t.getMessage();
        else if (t instanceof TransactionException)
            error = environment.getProperty("customerService.exception.transaction") + t.getMessage();
        else if (t.getCause() instanceof InvalidFormatException)
            error = environment.getProperty("customerService.exception.invalidInputFormat");
        else if (t instanceof javax.persistence.OptimisticLockException)
            error = environment.getProperty("customerService.exception.versionConflict");
        else
            error = environment.getProperty("customerService.exception.general") + t.getMessage();

        ResponseDto responseObject = new ResponseDto(ResponseStatus.Error, null, null, new ResponseException(error));
        return new ResponseEntity<>(responseObject, HttpStatus.OK);

    }
}
