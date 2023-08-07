package com.example.tenrello.entity;

import jakarta.persistence.*;

@Entity
@Table(name="column")
public class ColumnEntity extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @ManyToOne
    @JoinColumn(name="board_id")
    private Board board;
}
