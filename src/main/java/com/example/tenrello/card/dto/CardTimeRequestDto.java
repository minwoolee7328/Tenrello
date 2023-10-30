package com.example.tenrello.card.dto;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class CardTimeRequestDto {
    private String startTime;
    private LocalDateTime endTime;

//    private boolean startTime;
//    private String endTime;
}
