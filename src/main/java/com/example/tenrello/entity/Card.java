package com.example.tenrello.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenerationTime;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "cards")
@NoArgsConstructor
public class Card extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저와 연결
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 컬럽과 연결
//    @ManyToOne
//    @JoinColumn(name = "column_id")
//    private ColumnEntity column;

    // 임시 컬럼
    @Column(name = "columnid", nullable = false)
    private Long columnid;

    // 카드 제목
    @Column(name = "title", nullable = false)
    private String title;

    // 카드 내용
    @Column(name = "content", nullable = true)
    private String content;

    // 카드 위치 내용
    @Column(name = "position", nullable = false)
    private int position;

    // 시작 데이터
    @Column(name = "startTime", nullable = true)
    private LocalDateTime startTime;

    // 마감시간 데이터
    @Column(name = "endTime", nullable = true)
    private LocalDateTime endTime;


    public Card(User user, Long column, String title, int position){
        this.user = user;
        this.columnid = column;
        this.title = title;
        this.position = position;
    }

    @Transactional
    public void updateTitle(String title){
        this.title = title;
    }
    @Transactional
    public void updateContent(String content){
        this.content = content;
    }

    @Transactional
    public void updatePosition(int position){
        this.position = position;
    }

    @Transactional
    public void updateColumnId(Long columnid){
        this.columnid = columnid;
    }

    @Transactional
    public void updateStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }

    @Transactional
    public void updateEndTime(LocalDateTime endTime){
        this.endTime = endTime;
    }
}
