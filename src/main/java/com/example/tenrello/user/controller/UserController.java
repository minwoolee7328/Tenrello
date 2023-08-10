package com.example.tenrello.user.controller;

import com.example.tenrello.common.dto.ApiResponseDto;
import com.example.tenrello.security.details.UserDetailsImpl;
import com.example.tenrello.user.CheckPasswordDto;
import com.example.tenrello.user.dto.NicknameRequestDto;
import com.example.tenrello.user.dto.PasswordRequestDto;
import com.example.tenrello.user.dto.ProfileResponseDto;
import com.example.tenrello.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j(topic = "UserController")
public class UserController {

    private final UserService userService;

    @DeleteMapping("")
    public ResponseEntity<ApiResponseDto> deleteUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CheckPasswordDto passwordDto
    ) {
        log.info("회원 탈퇴 컨트롤러");
        userService.deleteUser(userDetails.getUser(), passwordDto);
        return ResponseEntity.ok().body(new ApiResponseDto("탈퇴 완료", HttpStatus.OK.value()));
    }

    @PutMapping("/nickname")
    public ResponseEntity<ProfileResponseDto> updateNickname(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody NicknameRequestDto requestDto
    ) {
        log.info("닉네임 변경 컨트롤러");
        ProfileResponseDto result = userService.updateNickname(userDetails.getUser(), requestDto);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResponseDto> updatePassword(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody PasswordRequestDto requestDto
            ) {
        log.info("비밀번호 변경 컨트롤러");
        userService.updatePassword(userDetails.getUser(), requestDto);
        return ResponseEntity.ok().body(new ApiResponseDto("비밀번호 변경 완료", HttpStatus.OK.value()));
    }
}
