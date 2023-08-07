package com.example.tenrello.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "board")
public class Board extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    @Column
    private String title;
    @Column
    private String description;
    //    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
    public Board(String title, String description) {
        this.title = title;
        this. description = description;
    }
}