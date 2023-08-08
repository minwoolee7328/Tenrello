package com.example.tenrello.auth.controller;

import com.example.tenrello.auth.dto.SigninRequestDto;
import com.example.tenrello.auth.dto.SignupRequestDto;
import com.example.tenrello.auth.service.AuthService;
import com.example.tenrello.common.dto.ApiResponseDto;
import com.example.tenrello.security.details.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j(topic = "AuthController")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        authService.signup(requestDto);
        return ResponseEntity.status(201).body(new ApiResponseDto("회원가입 성공", HttpStatus.CREATED.value()));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponseDto> login(@RequestBody SigninRequestDto requestDto, HttpServletResponse response) {
        authService.login(requestDto, response);
        return ResponseEntity.status(200).body(new ApiResponseDto("로그인 성공", HttpStatus.OK.value()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto> logout(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletRequest request,
            HttpServletResponse response) {
        log.info("로그아웃 컨트롤러");
        authService.logout(userDetails.getUser(), request, response);
        return ResponseEntity.ok().body(new ApiResponseDto("로그아웃 완료", HttpStatus.OK.value()));
    }
}

