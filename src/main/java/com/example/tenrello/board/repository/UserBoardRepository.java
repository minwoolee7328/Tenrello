package com.example.tenrello.board.repository;

import com.example.tenrello.entity.UserBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserBoardRepository extends JpaRepository<UserBoard, Long> {
    Optional<UserBoard> findByBoardIdAndUserId(Long boardId, Long userId);

    @Query("SELECT ub FROM UserBoard ub JOIN FETCH ub.board WHERE ub.user.id = :userId")
    List<UserBoard> findAllByUserIdWithUserFetch(Long userId);

}
