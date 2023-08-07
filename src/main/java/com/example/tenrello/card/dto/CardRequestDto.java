package com.example.tenrello.card.dto;

import lombok.Getter;

@Getter
public class CardRequestDto {
    private Long id;
    private String title;
    private String content;
}
