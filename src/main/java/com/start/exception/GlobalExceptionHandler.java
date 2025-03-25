package com.start.exception;

import com.mysql.cj.util.StringUtils;
import com.start.entitle.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        e.printStackTrace();
        String message = Objects.requireNonNullElse(e.getMessage(),
                String.format("未知错误（类型：%s）", e.getClass().getSimpleName()));
        return Result.error(message);
    }
}
