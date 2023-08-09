package com.example.tenrello.entity;


import com.example.tenrello.column.dto.ColumnRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="columns")
@Getter
@Setter
public class ColumnEntity extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        //id

    @Column(nullable = false)
    private String title;       //제목

    @ManyToOne
    @JoinColumn(name="board_id")
    private Board board;        //보드

    @Column(name="prev_column")
    private Long prevColumn;

    @Column(name="next_column")
    private Long nextColumn;

    @Column(name="firstnode")
    private Long firstnode;     //firstnode = 0
    @Column(name="lastnode")
    private Long lastnode;      //lastnode =1


    public void setTitle(String title){
        this.title =title;
    }

    public ColumnEntity(Board board, ColumnRequestDto columnRequestDto) {
        this.board = board;
        this.title = columnRequestDto.getTitle();

//        this.prevColumn = columnRequestDto.getPrev_column();
//        this.nextColumn = columnRequestDto.getNext_column();
//
//        this. firstnode = columnRequestDto.getFirstnode();
//        this.lastnode = columnRequestDto.getLastnode();
    }

    public ColumnEntity() {

    }
}
