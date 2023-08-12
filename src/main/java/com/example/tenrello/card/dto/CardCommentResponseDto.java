package com.example.tenrello.card.dto;

import com.example.tenrello.entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardCommentResponseDto {
    private Long id;
    private String comment;
    private String username;

    public CardCommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.comment = comment.getContent();
        this.username = comment.getUser().getUsername();
    }
}
