package com.example.tenrello.card.dto;

import com.example.tenrello.entity.Card;
import com.example.tenrello.entity.UserBoard;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BordUsersResponseDto {
    private List<BordUsersResponseListDto> userList = new ArrayList<>();

    public BordUsersResponseDto(List<UserBoard> userList){
        setCardList(userList);
    }

    public void setCardList(List<UserBoard> userList){
        for (UserBoard user:userList){
           this.userList.add(new BordUsersResponseListDto(user));
        }
    }

}
