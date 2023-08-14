package com.example.tenrello.card.repository;

import com.example.tenrello.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card,Long> {
    List<Card> findAllByOrderByColumn_id();

    List<Card> findAllByColumn_idOrderByPosition(Long Column_i);
}
