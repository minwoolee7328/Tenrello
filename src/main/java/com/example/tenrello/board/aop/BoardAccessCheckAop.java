package com.example.tenrello.board.aop;

import com.example.tenrello.board.repository.UserBoardRepository;
import com.example.tenrello.board.service.BoardServiceImpl;
import com.example.tenrello.common.exception.BoardAccessException;
import com.example.tenrello.common.exception.BoardAuthException;
import com.example.tenrello.entity.Board;
import com.example.tenrello.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j(topic = "executeBoardAuthCheck")
@Aspect
@Component
@RequiredArgsConstructor
public class BoardAccessCheckAop {

    private final BoardServiceImpl boardService;
    private final UserBoardRepository userBoardRepository;

    @Pointcut("@annotation(com.example.tenrello.board.aop.AccessCheck)")
    private void accessCheckMethods() {
    }

    @Around("accessCheckMethods()")
    public Object boardAccessCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

            User user = (User) joinPoint.getArgs()[0];
            Long boardId = (Long) joinPoint.getArgs()[1];

            Board board = boardService.findByBoard(boardId);

            if (userBoardRepository.findByBoardIdAndUserId(boardId, user.getId()).isEmpty()) {
                log.warn("초대 받지 못한 유저입니다.");
                throw new BoardAccessException("초대 받지 못한 유저입니다.");
            }

        return joinPoint.proceed();
    }

    @Pointcut("@annotation(com.example.tenrello.board.aop.AuthCheck)")
    private void authCheckMethods() {
    }

    @Around("authCheckMethods()")
    public Object boardAuthCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        User user = (User) joinPoint.getArgs()[0];
        Long boardId = (Long) joinPoint.getArgs()[1];

        Board board = boardService.findByBoard(boardId);

        if (!board.getUser().getId().equals(user.getId())) {
            log.warn("관리자만 삭제 및 초대, 권한부여가 가능합니다.");
            throw new BoardAuthException("관리자만 삭제 및 초대, 권한부여가 가능합니다.");
        }

        return joinPoint.proceed();
    }
}
