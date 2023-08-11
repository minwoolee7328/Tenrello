package com.example.tenrello.board.dto;

import com.example.tenrello.entity.User;
import lombok.Getter;

@Getter
public class MemberUserDto {
    private Long userId;
    private String username;
    private String nickname;

    public MemberUserDto (User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
    }
}
