package com.github.aliwocha.taskmanager.exception.role;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Role not found")
public class RoleNotFoundException extends RuntimeException {
}
