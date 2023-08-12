package com.example.tenrello.card.dto;

import com.example.tenrello.entity.UserBoard;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BordUsersResponseListDto {
    private Long userId;
    private String username;

    public BordUsersResponseListDto(UserBoard userList){
        this.userId = userList.getUser().getId();
        this.username = userList.getUser().getUsername();
    }
}
