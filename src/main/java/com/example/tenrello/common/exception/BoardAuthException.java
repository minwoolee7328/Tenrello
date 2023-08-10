package com.example.tenrello.common.exception;

/*관리자 권한*/
public class BoardAuthException extends RuntimeException{
    public BoardAuthException(String message) {
        super(message);
    }
}
