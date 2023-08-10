package com.example.tenrello.board.service;


import com.example.tenrello.board.dto.BoardRequestDto;
import com.example.tenrello.board.dto.BoardResponseDto;
import com.example.tenrello.board.dto.UserBoardResponseDto;
import com.example.tenrello.entity.User;

import java.util.List;

public interface BoardService {
    /**
     * 보드 생성
     * @param requestDto
     * @param user
     * @return
     */
    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user);

    /**
     * 사용자가 속한 보드 조회
     *
     * @param user
     * @return
     */
    List<UserBoardResponseDto> getUserIncludeBoard(User user);

    /**
     * 보드 수정
     * @param boardId
     * @param requestDto
     * @param user
     * @return
     */
    BoardResponseDto updateBoard(User user, Long boardId, BoardRequestDto requestDto);

    /**
     * 보드 삭제
     * @param boardId
     * @param user
     */
    void deleteBoard(User user, Long boardId);

    /**
     * 보드 초대
     * @param boardId
     * @param invitedUserId
     * @param user
     */
    void inviteUserToBoard(User user, Long boardId, Long invitedUserId);

    /**
     * 권한 부여
     * @param userId
     * @param user
     */
    void changeUserRoleInBoard(User user, Long boardId, Long userId);


}
