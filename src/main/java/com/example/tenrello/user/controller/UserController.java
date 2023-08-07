package com.example.tenrello.user.controller;

import com.example.tenrello.common.dto.ApiResponseDto;
import com.example.tenrello.security.details.UserDetailsImpl;
import com.example.tenrello.user.CheckPasswordDto;
import com.example.tenrello.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j(topic = "UserController")
public class UserController {

    private final UserService userService;

    @DeleteMapping("")
    public ResponseEntity<ApiResponseDto> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CheckPasswordDto passwordDto) {
        userService.deleteUser(userDetails.getUser(), passwordDto);
        return ResponseEntity.ok().body(new ApiResponseDto("탈퇴 완료", HttpStatus.OK.value()));
    }
}
