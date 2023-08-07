package com.example.tenrello.auth.service;

import com.example.tenrello.auth.dto.SigninRequestDto;
import com.example.tenrello.auth.dto.SignupRequestDto;
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
     * @param requestDto 로그인할 회원 정보
     * @param response   쿠키 반환
     */

    void login(SigninRequestDto requestDto, HttpServletResponse response);
}
