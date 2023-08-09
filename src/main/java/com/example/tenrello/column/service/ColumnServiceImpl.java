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

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ColumnServiceImpl implements ColumnService{

    private final BoardRepository boardRepository;
    private final ColumnRepository columnRepository;


    @Override
    @Transactional
    public ResponseEntity<ApiResponseDto> createcolumn(Long boardId, ColumnRequestDto  columnRequestDto) {
        Board board = findBoard(boardId);
        if(columnRepository.findAllByBoardId(boardId).isEmpty()){// 해당 Board의 컬럼 DB가 비었을 때

            ColumnEntity column = new ColumnEntity(board,columnRequestDto);
            column.setPrevColumn(null);                         //첫 컬럼 생성 이므로 다음과 이전 컬럼 null
            column.setNextColumn(null);
            column.setFirstnode(1L);                            //첫 마지막 노드 설정
            column.setLastnode(1L);

            columnRepository.save(column);                      //저장
        }
        else{
            ColumnEntity column = new ColumnEntity(board,columnRequestDto);
            ColumnEntity lastColumn = columnRepository.findByLastnode(1L);      //마지막 컬럼 찾기
            columnRepository.save(column);                                      //컬럼 저장

            Long id =column.getId();                                            //컬럼 id받아와서 마지막 컬럼의 다음 컬럼을 가리키는 id값에 넣어주기
            lastColumn.setNextColumn(id);
            lastColumn.setLastnode(0L);                                         //마지막 컬럼 설정풀기, 등록할 컬럼이 마지막 컬럼이 될 것이기 때문

            column.setPrevColumn(lastColumn.getId());                           //등록한 컬럼의 이전컬럼 설정
            column.setLastnode(1L);                                             //등록한 컬럼을 마지막 컬럼으로 설정
            column.setNextColumn(null);                                         //등록한 컬럼이 마지막 컬럼이기에 다음 컬럼 null
            column.setFirstnode(0L);                                            //첫 노드는 아니기에 첫 노드 설정 풀기


        }


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
    @Transactional
    public ResponseEntity<ApiResponseDto> deleteColumnName(Long columnId, UserDetailsImpl userDetails) {
        ColumnEntity column = columnRepository.findById(columnId).orElse(null);

        if(column == null) {
            throw new IllegalArgumentException("해당 컬럼이 존재하지 않습니다.");
        }
        if(column.getLastnode().equals(1L)){    //해당 컬럼이 마지막 컬럼일 때
            if(column.getPrevColumn() == null){ //해당 컬럼이 마지막 컬럼이면서 첫 컬럼일 때
                columnRepository.delete(column);    //컬럼 삭제
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("컬럼 삭제 성공 1",HttpStatus.OK.value()));
            }
            ColumnEntity prev = findColumn(column.getPrevColumn()); //내 이전 컬럼 찾아오기
            prev.setNextColumn(null);                               //이전 컬럼의 다음 컬럼을 가리키는 id값 null
            prev.setLastnode(1L);                                   //마지막 노드로 변경
            columnRepository.delete(column);                        //컬럼 삭제
        }
        else if(column.getFirstnode().equals(1L)){  //해당 컬럼이 첫 컬럼일 때
            ColumnEntity next = findColumn(column.getNextColumn()); //내 다음 컬럼 찾아오기
            next.setPrevColumn(null);                               //다음 컬럼의 이전 컬럼을 가리키는 id값 null
            next.setFirstnode(1L);                                  //첫 노드로 변경
            columnRepository.delete(column);                        //컬럼 삭제
        }
        //유저 권한이 Admin이 아니라면 삭제하지 않기
        else {                                                      //첫 컬럼도 마지막 컬럼도 아닐 때
            ColumnEntity prevColumn = findColumn(column.getPrevColumn());  //이전 컬럼 받아오기
            ColumnEntity nextColumn = findColumn(column.getNextColumn());   //다음 컬럼 받아오기

            prevColumn.setNextColumn(nextColumn.getId());               //삭제할 컬럼 양쪽 컬럼들을 연결
            nextColumn.setPrevColumn(prevColumn.getId());

            columnRepository.delete(column);                            //컬럼 삭제
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("컬럼 삭제 성공", HttpStatus.OK.value()));

    }


    @Override
    public List<ColumnResponseDto> getBoardColumn(Long boardId) {
        List<ColumnResponseDto> columnResponseDtoList = new ArrayList<>();

        ColumnEntity head;  //현재 찾을 헤드
        ColumnEntity firstColumn = columnRepository.findAllByBoardIdAndFirstnode(boardId,1L);//첫 컬럼 찾기
        head = firstColumn;
        while(true) {       //아래 break문에 걸릴 때까지 반복
            columnResponseDtoList.add(new ColumnResponseDto(head));//내보내줄 List에 해당 Entity변환해서 추가
            if(head.getNextColumn() == null)                        //헤드 다음 컬럼이 null이면 반복 나가기
                break;
            head = findColumn(head.getNextColumn());                //헤드 다음 컬럼으로 넘기기
        }

//        List<ColumnResponseDto> columnEntities = columnRepository
//                .findAllByBoardId(boardId).stream()
//                .map(ColumnResponseDto::new)
//                .toList();

        return columnResponseDtoList;
    }

    public Board findBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 board는 존재하지 않습니다."));
    }
    public ColumnEntity findColumn(Long id) {
        return columnRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 column은 존재하지 않습니다."));
    }
}
