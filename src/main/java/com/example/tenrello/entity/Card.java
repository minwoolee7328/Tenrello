package com.example.tenrello.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenerationTime;
import org.springframework.transaction.annotation.Transactional;

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

    public Card(User user, Long column, String title){
        this.user = user;
        this.columnid = column;
        this.title = title;
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
    public void updatePosition(Long position){
        this.position = position;
    }
}
