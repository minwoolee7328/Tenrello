package com.example.tenrello.board.dto;

import com.example.tenrello.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private Long userId;
    private Long boardId;
    private String title;
    private String description;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

//    private List<ColumnResponseDto> columnEntities = new LinkedList<>();

    public BoardResponseDto(Board board) {
        this.userId = board.getUser().getId();
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.createAt = board.getCreateAt();
        this.modifiedAt = board.getModifiedAt();
    }
}
