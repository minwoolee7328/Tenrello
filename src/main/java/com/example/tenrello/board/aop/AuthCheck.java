package com.example.tenrello.board.aop;
import java.lang.annotation.*;

/*
 * 커스텀 어노테이션 정의
 * 초대, 삭제, 권한부여 체크 어노테이션
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {
}