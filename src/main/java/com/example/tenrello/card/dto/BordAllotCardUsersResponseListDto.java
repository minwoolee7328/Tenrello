package com.example.tenrello.card.dto;

import com.example.tenrello.entity.UserCard;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BordAllotCardUsersResponseListDto {
    private Long userId;
    private Long cardId;
    private String userName;
    public BordAllotCardUsersResponseListDto(UserCard UserCards){
        this.userId = UserCards.getUser().getId();
        this.cardId = UserCards.getCard().getId();
        this.userName = UserCards.getUser().getUsername();
    }
}
