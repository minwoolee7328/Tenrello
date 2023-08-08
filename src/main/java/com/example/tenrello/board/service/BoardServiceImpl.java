package com.example.tenrello.board.service;

import com.example.tenrello.board.dto.BoardRequestDto;
import com.example.tenrello.board.dto.BoardResponseDto;
import com.example.tenrello.board.repository.BoardRepository;
import com.example.tenrello.board.repository.UserBoardRepository;
import com.example.tenrello.entity.Board;
import com.example.tenrello.entity.UserBoard;
import com.example.tenrello.entity.UserRoleEnum;
import com.example.tenrello.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.example.tenrello.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final UserBoardRepository userBoardRepository;
    private final UserRepository userRepository;

    @Override
    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {
        Board board = new Board(requestDto, user);
        boardRepository.save(board);

        UserBoard userBoard = new UserBoard(UserRoleEnum.ADMIN, board, user);
        userBoardRepository.save(userBoard);
        return new BoardResponseDto(board);
    }

    @Override
    public BoardResponseDto getOneBoard(Long boardId, User user) {
        Board board = findByBoard(boardId);

        BoardResponseDto responseDto = new BoardResponseDto(board);
        return responseDto;
    }

    @Override
    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto, User user) {
        Board board = findByBoard(boardId);
        board.updateBoard(requestDto);

        return new BoardResponseDto(board);
    }

    @Override
    public void deleteBoard(Long boardId, User user) {
        Board board = findByBoard(boardId);

        if (!user.getId().equals(board.getUser().getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        boardRepository.delete(board);
    }

//    @Override
//    public void roleChange(Long userId, User user) {
//
//        User username = userRepository.findByUsername()
//
//    }

    public Board findByBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 보드 입니다."));
    }
}
