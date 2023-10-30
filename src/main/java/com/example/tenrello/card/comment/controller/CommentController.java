package com.example.tenrello.card.comment.controller;

import com.example.tenrello.card.comment.dto.CommentRequestDto;
import com.example.tenrello.card.comment.dto.CommentResponseDto;
import com.example.tenrello.card.comment.service.CommentService;
import com.example.tenrello.card.dto.CardCommentResponseDto;
import com.example.tenrello.common.dto.ApiResponseDto;
import com.example.tenrello.security.details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j(topic = "CommentController")
public class CommentController {

    private final CommentService commentService;

    //댓글 생성 (프론트 완료)
    @PostMapping("/comment/cards/{id}")
    public CardCommentResponseDto createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.createComment(id,requestDto, userDetails);
    }

    // 댓글 수정 (프론트 완료)
    @PutMapping("/comments/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
       return commentService.updateComment(id, requestDto, userDetails);
    }

    // 댓글 삭제 (프론트 완료)
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponseDto> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(id,userDetails);
       return ResponseEntity.status(201).body(new ApiResponseDto("댓글 삭제 성공", HttpStatus.OK.value()));
    }
}
