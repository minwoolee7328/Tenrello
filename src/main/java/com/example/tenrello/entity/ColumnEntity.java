package com.example.tenrello.entity;


import com.example.tenrello.column.dto.ColumnRequestDto;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name="columns")
@Getter
public class ColumnEntity extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        //id

    @Column(nullable = false)
    private String title;       //제목

    @ManyToOne
    @JoinColumn(name="board_id")
    private Board board;        //보드

    public void setTitle(String title){
        this.title =title;
    }

    public ColumnEntity(Board board, ColumnRequestDto columnRequestDto) {
        this.board = board;
        this.title = columnRequestDto.getTitle();
    }

    public ColumnEntity() {

    }
}
