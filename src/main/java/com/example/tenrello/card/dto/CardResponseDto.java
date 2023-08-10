package com.example.tenrello.card.dto;

import com.example.tenrello.entity.Card;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CardResponseDto {
    private Long id;
    private LocalDateTime createAt;
    private String title;
    private String content;
    private String username;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<CardResponseListDto> cardList = new ArrayList<>();

    public CardResponseDto(Card card){
        this.id = card.getId();
        this.createAt = card.getCreatedAt();
        this.title = card.getTitle();
        this.content = card.getContent();
        this.username = card.getUser().getUsername();
        this.startTime = card.getStartTime();
        this.endTime = card.getEndTime();
    }
    public CardResponseDto(List<Card> cards){
       setCardList(cards);
    }

    public void setCardList(List<Card> cards){
        for (Card card :cards){
            cardList.add(new CardResponseListDto(card));
        }
    }
}
