package com.example.tenrello.entity;

import com.example.tenrello.board.dto.BoardRequestDto;
import com.fasterxml.jackson.databind.util.LinkedNode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "board")
public class Board extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private String color;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<UserBoard> userBoardList = new ArrayList<>();


    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private LinkedList<ColumnEntity> columnEntityLinkedList = new LinkedList<>();        //Column의 순서를 가지게 해줄 링크드리스트

    public Board(BoardRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.user = user;
        columnEntityLinkedList = new LinkedList<>();  //임시
    }

    public void updateBoard(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
    }
}
