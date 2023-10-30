package com.example.tenrello.user.dto;

import com.example.tenrello.entity.User;
import lombok.Getter;

@Getter
public class LoginUserProfileDto {
    private Long userId;
    private String username;
    private String nickname;
    public LoginUserProfileDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }
}
