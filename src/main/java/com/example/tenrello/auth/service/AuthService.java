package com.example.tenrello.auth.service;

import com.example.tenrello.auth.dto.SigninRequestDto;
import com.example.tenrello.auth.dto.SignupRequestDto;
import com.example.tenrello.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    /**
     * 회원가입
     *
     * @param requestDto 가입할 회원 정보
     */
    void signup(SignupRequestDto requestDto);

    /**
     * 로그인
     *
     * @param requestDto 로그인 할 회원 정보
     * @param response   쿠키 반환
     */

    void login(SigninRequestDto requestDto, HttpServletResponse response);

    /**
     * 로그아웃
     *
     * @param user    로그아웃 할 회원
     * @param request 토큰이 담긴 요청
     */
    void logout(User user, HttpServletRequest request, HttpServletResponse response);
}
