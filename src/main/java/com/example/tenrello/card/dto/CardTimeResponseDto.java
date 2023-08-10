package com.example.tenrello.card.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CardTimeResponseDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;


    public CardTimeResponseDto(LocalDateTime endTime){
        this.endTime = endTime;
    }
    public CardTimeResponseDto(LocalDateTime startTime,LocalDateTime endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
