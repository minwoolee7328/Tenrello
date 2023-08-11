package com.example.tenrello.column.dto;

import com.example.tenrello.board.dto.BoardResponseDto;
import com.example.tenrello.card.dto.CardResponseDto;
import com.example.tenrello.entity.Board;
import com.example.tenrello.entity.Card;
import com.example.tenrello.entity.ColumnEntity;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ColumnResponseDto {
    Long id;
    String title;
    BoardResponseDto board;

//    private Long prevColumn;    //Node
//    private Long nextColumn;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CardResponseDto> cardList = new ArrayList<>();

    public ColumnResponseDto(ColumnEntity column){
        this.id = column.getId();
        this.title = column.getTitle();
        this.board = new BoardResponseDto(column.getBoard());
        this.createdAt = column.getCreatedAt();
        this.modifiedAt = column.getModifiedAt();
        setCardList(column.getCardList());
    }

    public void setCardList(List<Card> cardList){
        for(Card card:cardList){
            this.cardList.add(new CardResponseDto(card));
        }
    }

}
