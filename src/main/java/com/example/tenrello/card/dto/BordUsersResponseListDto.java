package com.example.tenrello.card.dto;

import com.example.tenrello.entity.UserBoard;
import com.example.tenrello.entity.UserCard;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
