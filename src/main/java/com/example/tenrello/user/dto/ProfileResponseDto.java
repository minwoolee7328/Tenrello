package com.example.tenrello.user.dto;

import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private String username;
    private String nicknameBefore;
    private String nicknameAfter;

    public ProfileResponseDto(String username, String nicknameBefore, String nicknameAfter) {
        this.username = username;
        this.nicknameBefore = nicknameBefore;
        this.nicknameAfter = nicknameAfter;
    }
}
