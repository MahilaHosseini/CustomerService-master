package com.customerService.app.dto;


public class ResponseDto<T> {
    private ResponseStatus status;
    private T responseObject;
    private String notificationMessage;
    private ResponseException exception;

    public ResponseDto() {
    }

    public ResponseDto(ResponseStatus status, T responseObject, String notificationMessage, ResponseException exception) {
        this.status = status;
        this.responseObject = responseObject;
        this.notificationMessage = notificationMessage;
        this.exception = exception;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public T getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(T responseObject) {
        this.responseObject = responseObject;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public ResponseException getException() {
        return exception;
    }

    public void setException(ResponseException exception) {
        this.exception = exception;
    }
}
