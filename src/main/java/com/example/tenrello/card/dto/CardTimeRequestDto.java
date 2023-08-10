package com.example.tenrello.card.dto;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class CardTimeRequestDto {
    private boolean startTime;
    private String endTime;

}
