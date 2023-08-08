package com.example.tenrello.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "user-board")
public class UserBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userBoard_id")
    private Long id;

    @Column(name = "user_role")
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public UserBoard(UserRoleEnum role, Board board, User user) {
        this.role = role;
        this.board = board;
        this.user = user;
    }
}
