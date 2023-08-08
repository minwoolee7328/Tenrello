package com.example.tenrello.board.service;


import com.example.tenrello.board.dto.BoardRequestDto;
import com.example.tenrello.board.dto.BoardResponseDto;
import com.example.tenrello.entity.User;

public interface BoardService {
    /**
     * 보드 생성
     * @param requestDto
     * @param user
     * @return
     */
    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user);

    /**
     * 보드 조회
     * @param boardId
     * @param user
     * @return
     */
    BoardResponseDto getOneBoard(Long boardId, User user);

    /**
     * 보드 수정
     * @param boardId
     * @param requestDto
     * @param user
     * @return
     */
    BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto, User user);

    /**
     * 보드 삭제
     * @param boardId
     * @param user
     */
    void deleteBoard(Long boardId, User user);

    /**
     * 권한 부여
     * @param userId
     * @param user
     */
//    void roleChange(Long userId, User user);
}
