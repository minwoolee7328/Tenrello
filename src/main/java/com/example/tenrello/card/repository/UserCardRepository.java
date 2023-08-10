package com.example.tenrello.card.repository;

import com.example.tenrello.entity.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCardRepository extends JpaRepository<UserCard,Long> {

    Optional<UserCard> findByCardIdAndUserId(Long id, Long userid);
}
