package com.customerService.app.dto;

public class ResponseException {
    private String fullMessage;

    public ResponseException() {
    }

    public ResponseException(String fullMessage) {
        this.fullMessage = fullMessage;
    }

    public String getFullMessage() {
        return fullMessage;
    }

    public void setFullMessage(String fullMessage) {
        this.fullMessage = fullMessage;
    }
}
