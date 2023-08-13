package com.example.tenrello.card.dto;

import com.example.tenrello.entity.Card;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CardTimeResponseDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String result;

    public CardTimeResponseDto(LocalDateTime endTime, String cardResult){
        this.endTime = endTime;
        this.result = cardResult;
    }
    public CardTimeResponseDto(LocalDateTime startTime,LocalDateTime endTime, String cardResult){
        this.startTime = startTime;
        this.endTime = endTime;
        this.result = cardResult;
    }
}
