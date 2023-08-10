package com.example.tenrello.board.repository;

import com.example.tenrello.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
