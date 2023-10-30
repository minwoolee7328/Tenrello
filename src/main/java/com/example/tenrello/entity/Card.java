package com.example.tenrello.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenerationTime;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @ManyToOne
    @JoinColumn(name = "column_id")
    private ColumnEntity column;

    // 카드 제목
    @Column(name = "title", nullable = false)
    private String title;

    // 카드 내용
    @Column(name = "content")
    private String content;

    // 카드 위치 내용
    @Column(name = "position", nullable = false)
    private int position;

    // 시작 데이터
    @Column(name = "startTime")
    private LocalDateTime startTime;

    // 마감시간 데이터
    @Column(name = "endTime")
    private LocalDateTime endTime;

    // 일정 결과
    @Column(name = "result")
    private String result;

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE)
    private List<UserCard> UserCardList = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE)
    private List<Comment> cardCommentList = new ArrayList<>();

    public Card(User user, ColumnEntity column, String title, int position){
        this.user = user;
        this.column = column;
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
    public void updateColumnId(ColumnEntity column){
        this.column = column;
    }

    @Transactional
    public void updateStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }

    @Transactional
    public void updateEndTime(LocalDateTime endTime){
        this.endTime = endTime;
    }

    @Transactional
    public void updateResult(String result){
        this.result = result;
    }

}
