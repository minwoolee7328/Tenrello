package com.example.tenrello.board.dto;

import com.example.tenrello.entity.User;
import com.example.tenrello.entity.UserBoard;
import com.example.tenrello.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class MemberUserDto {
    private Long userId;
    private String username;
    private UserRoleEnum role;

    public MemberUserDto (UserBoard userBoard) {
        this.userId = userBoard.getUser().getId();
        this.username = userBoard.getUsername();
        this.role = userBoard.getRole();
    }
}
