package com.example.tenrello.card.dto;

import com.example.tenrello.entity.Card;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CardResponseDto {
    private Long id;
    private LocalDateTime createAt;
    private String title;
    private String username;

    public CardResponseDto(Card card){
        this.id = card.getId();
        this.createAt = card.getCreatedAt();
        this.title = card.getTitle();
        this.username = card.getUser().getUsername();
    }
}
