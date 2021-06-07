package com.github.aliwocha.taskmanager.exception.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CategoryForbiddenException extends RuntimeException {

    public CategoryForbiddenException(String message) {
        super(message);
    }
}
