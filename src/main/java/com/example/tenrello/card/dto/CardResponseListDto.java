package com.example.tenrello.card.dto;


import com.example.tenrello.entity.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class CardResponseListDto {

    private String cardTitle;

    public CardResponseListDto(Card cards){
        this.cardTitle = cards.getTitle();
    }
}
