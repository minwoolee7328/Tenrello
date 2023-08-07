package com.example.tenrello.card.repository;

import com.example.tenrello.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card,Long> {
}
