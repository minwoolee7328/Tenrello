package com.example.tenrello.board.aop;

import java.lang.annotation.*;

/*
* 커스텀 어노테이션 정의
* userBoardRepository 에 저장되지 않은 유저 체크
*/
@Target(ElementType.METHOD) /*ExcludeLogging 어노테이션을 메서드에만 적용할 수 있도록 지정*/
@Retention(RetentionPolicy.RUNTIME) /*ExcludeLogging 어노테이션이 런타임까지 유지되도록 지정*/
public @interface AccessCheck {
}
