package com.example.tenrello.common.exception;

import com.example.tenrello.common.dto.ApiResponseDto;
import jakarta.security.auth.message.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiResponseDto> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        ApiResponseDto apiResponseDto = new ApiResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                apiResponseDto,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({BoardAuthException.class})
    public ResponseEntity<ApiResponseDto> authExceptionHandler(BoardAuthException ex) {
        ApiResponseDto apiResponseDto = new ApiResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                apiResponseDto,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ApiResponseDto> notFoundBoardExceptionHandler(NotFoundException ex) {
        ApiResponseDto apiResponseDto = new ApiResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                apiResponseDto,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({BoardAccessException.class})
    public ResponseEntity<ApiResponseDto> accessDeniedExceptionHandler(BoardAccessException ex) {
        ApiResponseDto apiResponseDto = new ApiResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                apiResponseDto,
                HttpStatus.BAD_REQUEST
        );
    }
}
