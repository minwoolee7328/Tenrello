package com.example.tenrello.user.dto;

import com.example.tenrello.entity.User;
import lombok.Getter;

@Getter
public class SimpleUserInfoDto {
    private Long userId;
    private String username;
    private String nickname;

    public SimpleUserInfoDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }
}
