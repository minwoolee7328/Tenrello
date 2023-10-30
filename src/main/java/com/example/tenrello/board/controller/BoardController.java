package com.example.tenrello.board.controller;

import com.example.tenrello.board.dto.BoardRequestDto;
import com.example.tenrello.board.dto.BoardResponseDto;
import com.example.tenrello.board.dto.MemberUserListResponseDto;
import com.example.tenrello.board.dto.UserBoardResponseDto;
import com.example.tenrello.board.service.BoardServiceImpl;
import com.example.tenrello.common.dto.ApiResponseDto;
import com.example.tenrello.security.details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardServiceImpl boardService;

    /*보드 생성*/
    @PostMapping("")
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        BoardResponseDto createBoard = boardService.createBoard(requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(createBoard);
    }

    /*사용자가 속한 보드 조회*/
    @GetMapping("/include")
    public List<UserBoardResponseDto> getUserIncludeBoard(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return boardService.getUserIncludeBoard(userDetails.getUser());
    }

    /*보드 수정*/
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long boardId,
                                                        @RequestBody BoardRequestDto requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardResponseDto updateBoard = boardService.updateBoard(userDetails.getUser(), boardId, requestDto);
        return ResponseEntity.ok().body(updateBoard);
    }

    /*보드 삭제*/
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponseDto> deleteBoard(@PathVariable Long boardId,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.deleteBoard(userDetails.getUser(), boardId);
        return ResponseEntity.ok().body(new ApiResponseDto("삭제 되었습니다.", HttpStatus.OK.value()));
    }

    /*보드 초대*/
    @PostMapping("/{boardId}/invite/{invitedUserId}")
    public ResponseEntity<ApiResponseDto> inviteUserToBoard(@PathVariable Long boardId,
                                                            @PathVariable Long invitedUserId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        boardService.inviteUserToBoard(userDetails.getUser(), boardId, invitedUserId);
        return ResponseEntity.ok().body(new ApiResponseDto("초대 완료되었습니다.", HttpStatus.OK.value()));
    }

    /*보드 권한 부여*/
    @PutMapping("/{boardId}/members/{userId}")
    public ResponseEntity<ApiResponseDto> changeUserRoleInBoard(@PathVariable Long boardId,
                                                                @PathVariable Long userId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

        boardService.changeUserRoleInBoard(userDetails.getUser(), boardId, userId);
        return ResponseEntity.ok().body(new ApiResponseDto("권한 변경 완료", HttpStatus.OK.value()));
    }

    /*보드에 속한 사용자 정보 조회*/
    @GetMapping("/{boardId}/members")
    public ResponseEntity<MemberUserListResponseDto> getMembersOfBoard(@PathVariable Long boardId) {
        MemberUserListResponseDto result = boardService.getMembersOfBoard(boardId);
        return ResponseEntity.ok().body(result);
    }
}
