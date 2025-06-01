package com.example.exception;

import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends ApplicationException {

    public PasswordMismatchException() {
        super("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}
