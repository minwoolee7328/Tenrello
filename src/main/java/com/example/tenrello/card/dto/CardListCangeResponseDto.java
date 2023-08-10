package com.example.tenrello.card.dto;

import com.example.tenrello.entity.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class CardListCangeResponseDto {

    private List<CardResponseListDto> cardList = new ArrayList<>();

    public CardListCangeResponseDto(List<Card> cards){
        setCardList(cards);
    }

    public void setCardList(List<Card> cards){
        for (Card card :cards){
            cardList.add(new CardResponseListDto(card));
        }
    }
}
