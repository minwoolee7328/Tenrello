package com.example.tenrello.column.service;

import com.example.tenrello.board.repository.BoardRepository;
import com.example.tenrello.column.dto.ColumnRequestDto;
import com.example.tenrello.column.dto.ColumnResponseDto;
import com.example.tenrello.column.repository.ColumnRepository;
import com.example.tenrello.common.dto.ApiResponseDto;
import com.example.tenrello.entity.Board;
import com.example.tenrello.entity.ColumnEntity;
import com.example.tenrello.entity.User;
import com.example.tenrello.security.details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ColumnServiceImpl implements ColumnService{

    private final BoardRepository boardRepository;
    private final ColumnRepository columnRepository;


    @Override
    public ResponseEntity<ApiResponseDto> createcolumn(Long boardId, ColumnRequestDto columnRequestDto) {
        Board board = findBoard(boardId);
        ColumnEntity column = new ColumnEntity(board,columnRequestDto);
        columnRepository.save(column);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("새 컬럼 등록 성공",HttpStatus.OK.value()));
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponseDto> updateColumnName(Long columnId, ColumnRequestDto columnRequestDto, User user) {
        ColumnEntity column = columnRepository.findById(columnId).orElse(null);

        if(column == null) {
            throw new IllegalArgumentException("해당 컬럼이 존재하지 않습니다.");
        }

        //해당 유저Role이 Member도 아니고 Admin도 아닐 시 User에 Role추가되면 수정
//        if() {
//            throw new IllegalArgumentException("해당 보드의 유저가 아닙니다.");
//        }
        column.setTitle(columnRequestDto.getTitle());


        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("컬럼제목 수정 성공", HttpStatus.OK.value()));
    }

    @Override
    public ResponseEntity<ApiResponseDto> deleteColumnName(Long columnId, UserDetailsImpl userDetails) {
        ColumnEntity column = columnRepository.findById(columnId).orElse(null);

        if(column == null) {
            throw new IllegalArgumentException("해당 컬럼이 존재하지 않습니다.");
        }

        //유저 권한이 Admin이 아니라면 삭제하지 않기

        columnRepository.delete(column);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("컬럼 삭제 성공",HttpStatus.OK.value()));
    }

//    @Override
//    public List<ColumnResponseDto> getBoardColumn(Long boardId) {
//
//        List<ColumnResponseDto> columnResponseDtoList = columnRepository
//                .findAllByBoardId(boardId)
//                .stream()
//                .map(ColumnResponseDto::new)
//                .toList();
//
//
//        return columnResponseDtoList;
//    }
    @Override
    public List<ColumnResponseDto> getBoardColumn(Long boardId) {
        List<ColumnResponseDto> columnEntities = columnRepository
                .findAllByBoardId(boardId).stream().map(ColumnResponseDto::new).toList();

        return columnEntities;
    }

    public Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 board는 존재하지 않습니다."));
    }
}
