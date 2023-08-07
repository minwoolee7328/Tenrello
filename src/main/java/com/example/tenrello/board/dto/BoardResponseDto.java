package com.example.tenrello.board.dto;

import lombok.Getter;

@Getter
public class BoardResponseDto {
    Long id;
    String title;
    String description;
    public BoardResponseDto(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
