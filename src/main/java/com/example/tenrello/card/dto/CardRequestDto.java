package com.example.tenrello.card.dto;

import lombok.Getter;

@Getter
public class CardRequestDto {
    private Long position;
    private String title;
    private String content;
    private Long columnId;
}
