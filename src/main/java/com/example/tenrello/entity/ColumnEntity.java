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
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name="board_id")
    private Board board;

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
