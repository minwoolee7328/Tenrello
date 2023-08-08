package com.example.tenrello.column.dto;

import com.example.tenrello.board.dto.BoardResponseDto;
import com.example.tenrello.entity.Board;
import com.example.tenrello.entity.ColumnEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ColumnResponseDto {
    Long id;
    String title;
    BoardResponseDto board;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ColumnResponseDto(ColumnEntity column){
        this.id = column.getId();
        this.title = column.getTitle();
        this.board = new BoardResponseDto(column.getBoard());
        this.createdAt = column.getCreatedAt();
        this.modifiedAt = column.getModifiedAt();
    }


}
