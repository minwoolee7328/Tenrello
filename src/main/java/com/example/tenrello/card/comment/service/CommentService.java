package com.example.tenrello.card.comment.service;

import com.example.tenrello.card.comment.dto.CommentRequestDto;
import com.example.tenrello.card.comment.dto.CommentResponseDto;
import com.example.tenrello.card.comment.repository.CommentRepository;
import com.example.tenrello.card.repository.CardRepository;
import com.example.tenrello.entity.Card;
import com.example.tenrello.entity.Comment;
import com.example.tenrello.entity.User;
import com.example.tenrello.security.details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    // 댓글 생성
    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        //카드 정보
        Optional<Card> card = cardRepository.findById(id);
        //유저 정보
        User user = userDetails.getUser();

        // 댓글 생성
        Comment commet = new Comment(user, card.get(), requestDto.getContent());

        commentRepository.save(commet);

        return new CommentResponseDto(commet.getContent());
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        // 댓글 확인
        Optional<Comment> comment = commentRepository.findById(id);

        if(!comment.isPresent()){
            // 댓글이 없다면
            throw new IllegalArgumentException("해달 댓글이 존재하지 않습니다.");
        }

        //유저 확인
        User user = userDetails.getUser();

        //사용자가 작성한 댓글만 수정 가능
        //(보드사용자는 모두 수정가능) - 미구현
        if(!user.getId().equals(comment.get().getUser().getId())){
            // 사용자가 작성한 댓글이 아님
            throw new IllegalArgumentException("사용자가 작성한 댓글이 아닙니다.");
        }

        //사용자가 작성한 댓글이면 수정
        comment.get().updateContent(requestDto.getContent());

        return new CommentResponseDto(comment.get().getContent());
    }

    // 댓글 삭제
    public void deleteComment(Long id, UserDetailsImpl userDetails) {
        // 댓글 확인
        Optional<Comment> comment = commentRepository.findById(id);

        if(!comment.isPresent()){
            // 댓글이 없다면
            throw new IllegalArgumentException("해달 댓글이 존재하지 않습니다.");
        }

        //유저 확인
        User user = userDetails.getUser();

        //사용자가 작성한 댓글만 삭제 가능
        //(보드사용자는 모두 수정가능) - 미구현
        if(!user.getId().equals(comment.get().getUser().getId())){
            // 사용자가 작성한 댓글이 아님
            throw new IllegalArgumentException("사용자가 작성한 댓글이 아닙니다.");
        }

        commentRepository.delete(comment.get());
    }
}
