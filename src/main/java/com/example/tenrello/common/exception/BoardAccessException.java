package com.example.tenrello.common.exception;

/*보드 입장 확인*/
public class BoardAccessException extends RuntimeException{
    public BoardAccessException(String message) {
        super(message);
    }
}
