package com.example.tenrello.board.controller;

import com.example.tenrello.board.dto.BoardRequestDto;
import com.example.tenrello.board.dto.BoardResponseDto;
import com.example.tenrello.board.service.BoardServiceImpl;
import com.example.tenrello.common.dto.ApiResponseDto;
import com.example.tenrello.security.details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardServiceImpl boardService;

    /*보드 생성*/
    @PostMapping("")
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto requestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        BoardResponseDto createBoard = boardService.createBoard(requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(createBoard);
    }

    /*보드 조회*/
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getOneBoard(@PathVariable Long boardId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardResponseDto getOneBoard = boardService.getOneBoard(boardId, userDetails.getUser());
        return ResponseEntity.ok().body(getOneBoard);
    }

    /*보드 수정*/
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long boardId,
                                          @RequestBody BoardRequestDto requestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardResponseDto updateBoard = boardService.updateBoard(boardId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(updateBoard);
    }

    /*보드 삭제*/
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponseDto> deleteBoard(@PathVariable Long boardId,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.deleteBoard(boardId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("삭제 되었습니다.", HttpStatus.OK.value()));
    }

    /*보드 권한 부여*/
    @PutMapping("/{userId}/member")
    public ResponseEntity<ApiResponseDto> roleChange(@PathVariable Long userId,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

//        UserRoleEnum role = userDetails.getUser().getRole();
//        boardService.roleChange(userId, role);
        return ResponseEntity.ok().body(new ApiResponseDto("권한 변경 완료", HttpStatus.OK.value()));
    }
}
