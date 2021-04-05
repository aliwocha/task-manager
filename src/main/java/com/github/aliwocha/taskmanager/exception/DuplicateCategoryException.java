package com.github.aliwocha.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Category with given name already exists")
public class DuplicateCategoryException extends RuntimeException {
}
