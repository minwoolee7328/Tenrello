package com.example.tenrello.card.repository;

import com.example.tenrello.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card,Long> {
    List<Card> findAllByColumnid(Long columnId);

    int findByColumnidAndPosition(Long columnId, Long id);
//    List<Card> findAllByTitle();
}
