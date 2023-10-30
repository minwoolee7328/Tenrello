package com.example.tenrello.column.repository;

import com.example.tenrello.entity.ColumnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnRepository extends JpaRepository<ColumnEntity,Long> {

    List<ColumnEntity> findAllByBoardId(Long boardId);

    ColumnEntity findByBoardIdAndLastnode(Long boardId, Long i);

    ColumnEntity findAllByBoardIdAndFirstnode(Long boardId, long l);
}
