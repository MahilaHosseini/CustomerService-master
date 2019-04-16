package com.customerService.app.dto;

import java.io.Serializable;

/**
 * Created by h.akbari
 * Date 5/27/17
 * Time 1:23 PM
 */
public class MessageDto<T, K> implements Serializable {
    private static final long serialVersionUID = 3042962802221340612L;

    private ResponseStatus status;
    private T messageObject;
    private K baseException;

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(final ResponseStatus status) {
        this.status = status;
    }

    public T getMessageObject() {
        return messageObject;
    }

    public void setMessageObject(final T messageObject) {
        this.messageObject = messageObject;
    }

    public K getBaseException() {
        return baseException;
    }

    public void setBaseException(final K baseException) {
        this.baseException = baseException;
    }
}
