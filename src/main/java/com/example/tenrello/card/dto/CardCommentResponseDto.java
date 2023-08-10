package com.example.tenrello.card.dto;

import com.example.tenrello.entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardCommentResponseDto {
    private String comment;

    public CardCommentResponseDto(Comment comment){
        this.comment = comment.getContent();
    }
}
