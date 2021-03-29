package com.github.aliwocha.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Category with such name already exists")
public class DuplicateCategoryNameException extends RuntimeException {
}
