package com.example.tenrello.board.service;

import com.example.tenrello.board.aop.AccessCheck;
import com.example.tenrello.board.aop.AuthCheck;
import com.example.tenrello.board.dto.*;
import com.example.tenrello.board.repository.BoardRepository;
import com.example.tenrello.board.repository.UserBoardRepository;
import com.example.tenrello.common.exception.NotFoundException;
import com.example.tenrello.entity.Board;
import com.example.tenrello.entity.User;
import com.example.tenrello.entity.UserBoard;
import com.example.tenrello.entity.UserRoleEnum;
import com.example.tenrello.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final UserBoardRepository userBoardRepository;
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    @Override
    public BoardResponseDto createBoard(BoardRequestDto requestDto, User user) {
        Board board = new Board(requestDto, user);
        boardRepository.save(board);

        UserBoard userBoard = new UserBoard(UserRoleEnum.ADMIN, board, user);
        userBoardRepository.save(userBoard);
        return new BoardResponseDto(board);
    }

    @Override
    public List<UserBoardResponseDto> getUserIncludeBoard(User user) {

        return userBoardRepository.findAllByUserIdWithUserFetch(user.getId())
                .stream()
                .map(UserBoardResponseDto::new)
                .toList();
    }

    @Override
    @AccessCheck
    @Transactional
    public BoardResponseDto updateBoard(User user, Long boardId, BoardRequestDto requestDto) {
        Board board = findByBoard(boardId);

        board.updateBoard(requestDto);
        return new BoardResponseDto(board);
    }

    @Override
    @AccessCheck
    @AuthCheck
    public void deleteBoard(User user, Long boardId) {
        Board board = findByBoard(boardId);

        boardRepository.delete(board);
    }

    @Override
    @AccessCheck
    @AuthCheck
    @Transactional
    public void inviteUserToBoard(User user, Long boardId, Long invitedUserId) {
        Board board = findByBoard(boardId);

        User invitedUser = userRepository.findById(invitedUserId)
                .orElseThrow(() -> new IllegalArgumentException("초대받은 유저를 찾을 수 없습니다."));

        Optional<UserBoard> userBoardOptional = userBoardRepository.findByBoardIdAndUserId(boardId, invitedUserId);
        if (userBoardOptional.isPresent()) {
            throw new IllegalArgumentException("이미 초대된 유저입니다.");
        }

        UserBoard userBoard = new UserBoard(UserRoleEnum.MEMBER, board, invitedUser);
        userBoardRepository.save(userBoard);
    }

    @Override
    @AccessCheck
    @AuthCheck
    @Transactional
    public void changeUserRoleInBoard(User user, Long boardId, Long userId) {
        Board board = findByBoard(boardId);

        /*보드와 유저에 대한 UserBoard 엔티티 조회*/
        Optional<UserBoard> userBoardOptional = userBoardRepository.findByBoardIdAndUserId(boardId, userId);

        if (userBoardOptional.isPresent()) {
            UserBoard userBoard = userBoardOptional.get();

            if (userBoard.getRole() == UserRoleEnum.ADMIN) {
                userBoard.setRole((UserRoleEnum.MEMBER));
            } else if (userBoard.getRole() == UserRoleEnum.MEMBER) {
                userBoard.setRole(UserRoleEnum.ADMIN);
            }
        } else {
            throw new IllegalArgumentException("해당 보드에 유저가 없습니다.");
        }
    }

    @Override
    public MemberUserListResponseDto getMembersOfBoard(Long boardId) {
        Board board = findByBoard(boardId);

        List<MemberUserDto> memberList = board.getUserBoardList()
                .stream()
                .map(UserBoard::getUser).map(MemberUserDto::new).toList();

        return new MemberUserListResponseDto(memberList);
    }

    public Board findByBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new NotFoundException(messageSource.getMessage(
                        "not.found.board",
                        null,
                        "Not found board",
                        Locale.getDefault()
                ))
        );
    }
}
