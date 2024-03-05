package com.renanbilhan.wfcourse.controller.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationError extends StandardError{

    @Serial
    private static final long serialVersionUID = 1L;

    private List<FieldError> errors = new ArrayList<>();
    public ValidationError(LocalDateTime timeStamp, String path, Integer status, String error, String message) {
        super(timeStamp, path, status, error, message);
    }

    public void addError(String fieldName, String message){
        this.errors.add(new FieldError(fieldName, message));
    }

    @AllArgsConstructor
    @Getter
    private static final class FieldError{
        private String fieldName;
        private String message;

    }
}
