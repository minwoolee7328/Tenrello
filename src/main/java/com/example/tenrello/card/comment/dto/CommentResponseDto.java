package com.example.tenrello.card.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDto {
    private String content;

    public CommentResponseDto(String content){
        this.content = content;
    }
}
