package com.example.tenrello.card.dto;

import com.example.tenrello.entity.Card;
import com.example.tenrello.entity.UserBoard;
import com.example.tenrello.entity.UserCard;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BordUsersResponseDto {
    private List<BordAllotCardUsersResponseListDto> allotCardUserList = new ArrayList<>();
    private List<BordUsersResponseListDto> userList = new ArrayList<>();

    public BordUsersResponseDto(List<UserBoard> userList, List<UserCard> UserCardList){
        setAllotCardList(UserCardList);
        setCardList(userList);
    }

    public void setCardList(List<UserBoard> userList){
        for (UserBoard user:userList){
           this.userList.add(new BordUsersResponseListDto(user));
        }
    }

    public void setAllotCardList(List<UserCard> UserCardList){
        for (UserCard UserCards:UserCardList){
            this.allotCardUserList.add(new BordAllotCardUsersResponseListDto(UserCards));
        }
    }

}
