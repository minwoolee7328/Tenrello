package com.example.tenrello.column.service;

import com.example.tenrello.board.repository.BoardRepository;
import com.example.tenrello.board.repository.UserBoardRepository;
import com.example.tenrello.column.dto.ColumnRequestDto;
import com.example.tenrello.column.dto.ColumnResponseDto;
import com.example.tenrello.column.repository.ColumnRepository;
import com.example.tenrello.common.dto.ApiResponseDto;
import com.example.tenrello.entity.*;
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
    private final UserBoardRepository userBoardRepository;

    @Override
    @Transactional      //컬럼 생성
    public ColumnResponseDto createcolumn(Long boardId, ColumnRequestDto  columnRequestDto) {
        Board board = findBoard(boardId);
        ColumnEntity column;
        if(columnRepository.findAllByBoardId(boardId).isEmpty()){// 해당 Board의 컬럼 DB가 비었을 때

            column = new ColumnEntity(board,columnRequestDto);
            column.setPrevColumn(null);                         //첫 컬럼 생성 이므로 다음과 이전 컬럼 null
            column.setNextColumn(null);
            column.setFirstnode(1L);                            //첫 마지막 노드 설정
            column.setLastnode(1L);

            columnRepository.save(column);                      //저장
        }
        else{
            column = new ColumnEntity(board,columnRequestDto);
            ColumnEntity lastColumn = columnRepository.findByBoardIdAndLastnode(boardId,1L);      //마지막 컬럼 찾기
            columnRepository.save(column);                                      //컬럼 저장

            Long id =column.getId();                                            //컬럼 id받아와서 마지막 컬럼의 다음 컬럼을 가리키는 id값에 넣어주기
            lastColumn.setNextColumn(id);
            lastColumn.setLastnode(0L);                                         //마지막 컬럼 설정풀기, 등록할 컬럼이 마지막 컬럼이 될 것이기 때문

            column.setPrevColumn(lastColumn.getId());                           //등록한 컬럼의 이전컬럼 설정
            column.setLastnode(1L);                                             //등록한 컬럼을 마지막 컬럼으로 설정
            column.setNextColumn(null);                                         //등록한 컬럼이 마지막 컬럼이기에 다음 컬럼 null
            column.setFirstnode(0L);                                            //첫 노드는 아니기에 첫 노드 설정 풀기


        }


        return new ColumnResponseDto(column);
    }

    @Override
    @Transactional          //컬럼 이름 수정
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
    @Transactional//  컬럼 삭제
    public ResponseEntity<ApiResponseDto> deleteColumnName(Long columnId, UserDetailsImpl userDetails) {
        ColumnEntity column = findColumn(columnId);

        UserBoard userBoard = userBoardRepository.findByBoardIdAndUserId(column.getBoard().getId(),userDetails.getUser().getId()).orElse(null);
        if(!userBoard.getRole().equals(UserRoleEnum.ADMIN)){
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("어드민이 아니어서 삭제할 수 없습니다.", HttpStatus.FORBIDDEN.value()));
        }

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
        else {                                                      //첫 컬럼도 마지막 컬럼도 아닐 때
            columnConnect(column);                                  //삭제될 컬럼 양쪽 컬럼 연결

            columnRepository.delete(column);                            //컬럼 삭제
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("컬럼 삭제 성공", HttpStatus.OK.value()));

    }


    @Override   //보드별 컬럼 전체 조회
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
    @Override
    @Transactional
    public ResponseEntity<ApiResponseDto> updateColumnPosition(Long columnPositionId, Long columnTargetPositionId){
//        오른쪽으로 옮기는 것만 신경써서 만든 부분 왼쪽으로 옮기는 건 생각해봐야함
        if(columnPositionId.equals(columnTargetPositionId)){    //같은 위치로 옮길 때, 컬럼이 하나뿐일 때도 포함
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("컬럼 이동 성공", HttpStatus.OK.value()));
        }
        ColumnEntity column = findColumn(columnPositionId);     //옮길 컬럼
        ColumnEntity targetColumn = findColumn(columnTargetPositionId);     //옮길위치의 컬럼
        ColumnEntity head = column;
        Boolean isRight = false;
        while(!(head.getNextColumn() == null)){
            if(head.getNextColumn().equals(targetColumn.getId())){
                isRight =true;
                break;
            }
            head = findColumn(head.getNextColumn());
        }

        if(column.getFirstnode().equals(1L)) {   //해당 컬럼이 첫 컬럼일 때
            if (targetColumn.getLastnode().equals(1L)) {            //타겟 컬럼이 마지막 컬럼일 때
                    ColumnEntity newFirstNode = findColumn(column.getNextColumn());
                    newFirstNode.setPrevColumn(null);
                    newFirstNode.setFirstnode(1L);

                    targetColumn.setNextColumn(column.getId());
                    targetColumn.setLastnode(0L);
                    column.setPrevColumn(targetColumn.getId());
                    column.setNextColumn(null);
                    column.setLastnode(1L);
                    column.setFirstnode(0L);

            }//타겟 컬럼이 마지막 컬럼일 때
            else{
                Long next = targetColumn.getNextColumn();       //target의 다음컬럼 id값 빼두기

                ColumnEntity newFirstNode = findColumn(column.getNextColumn());
                newFirstNode.setPrevColumn(null);
                newFirstNode.setFirstnode(1L);

                column.setFirstnode(0L);
                column.setPrevColumn(targetColumn.getId());
                column.setNextColumn(next);
                targetColumn.setNextColumn(column.getId());
                findColumn(next).setPrevColumn(column.getId());

            }
        }   //옮길 컬럼이 첫 컬럼일 때
        else if(column.getLastnode().equals(1L)) {  //해당 컬럼이 마지막 컬럼일 때
            if (targetColumn.getFirstnode().equals(1L)) {   //타겟 컬럼이 첫 컬럼일 때

                ColumnEntity newLastNode = findColumn(column.getPrevColumn());
                newLastNode.setNextColumn(null);
                newLastNode.setLastnode(1L);

                targetColumn.setPrevColumn(column.getId());
                targetColumn.setFirstnode(0L);
                column.setNextColumn(targetColumn.getId());
                column.setPrevColumn(null);
                column.setLastnode(0L);
                column.setFirstnode(1L);
            }//타겟 컬럼이 첫 컬럼일 때
            else{
                Long prev = targetColumn.getPrevColumn();       //target의 다음컬럼 id값 빼두기

                ColumnEntity newLastNode = findColumn(column.getPrevColumn());
                newLastNode.setNextColumn(null);
                newLastNode.setLastnode(1L);

                column.setLastnode(0L);
                column.setNextColumn(targetColumn.getId());
                column.setPrevColumn(prev);
                targetColumn.setPrevColumn(column.getId());
                findColumn(prev).setNextColumn(column.getId());
            }

        }       //옮길 컬럼이 마지막 컬럼일 때
        else if(targetColumn.getFirstnode().equals(1L)){     //옮길 위치가 첫 노드일 때
            columnConnect(column);
            targetColumn.setPrevColumn(column.getId());
            targetColumn.setFirstnode(0L);
            column.setNextColumn(targetColumn.getId());
            column.setPrevColumn(null);
            column.setFirstnode(1L);
        }
        else if(targetColumn.getLastnode().equals(1L)){     //옮길 위치가 마지막 노드일 때
            columnConnect(column);
            targetColumn.setNextColumn(column.getId());
            targetColumn.setLastnode(0L);
            column.setPrevColumn(targetColumn.getId());
            column.setNextColumn(null);
            column.setLastnode(1L);
        }
        else if(isRight){
            columnConnect(column);           //양쪽 컬럼 연결

            Long targetNextId =targetColumn.getNextColumn();   //옮길 위치의 컬럼 다음 id 저장해두기 temp같은 역할
            targetColumn.setNextColumn(column.getId());              //옮길 위치걸럼 다음 컬럼 값을 옮긴 컬럼으로 바꿔주기
            findColumn(targetNextId).setPrevColumn(column.getId());
            column.setPrevColumn(targetColumn.getId());             //옮긴 컬럼 이전 컬럼 값을 옮길 위치 컬럼으로 변경
            column.setNextColumn(targetNextId);                     //저장해뒀던 id 값 옮긴 컬럼 다음 컬럼id값으로 저왼
        }       //그 외 중간 컬럼들 아직 오른쪽 이동만 생각한...
        else if(!isRight){
            columnConnect(column);           //양쪽 컬럼 연결

            Long targetPrevId =targetColumn.getPrevColumn();   //옮길 위치의 컬럼 다음 id 저장해두기 temp같은 역할
            targetColumn.setPrevColumn(column.getId());              //옮길 위치걸럼 다음 컬럼 값을 옮긴 컬럼으로 바꿔주기
            findColumn(targetPrevId).setNextColumn(column.getId());
            column.setNextColumn(targetColumn.getId());             //옮긴 컬럼 이전 컬럼 값을 옮길 위치 컬럼으로 변경
            column.setPrevColumn(targetPrevId);                     //저장해뒀던 id 값 옮긴 컬럼 다음 컬럼id값으로 저왼
        }


        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto("컬럼 이동 성공", HttpStatus.OK.value()));
    }
    public void columnConnect(ColumnEntity column){
        findColumn(column.getPrevColumn()).setNextColumn(column.getNextColumn());   //컬럼이 옮겨진 자리 양쪽 컬럼 연결해주기
        findColumn(column.getNextColumn()).setPrevColumn(column.getPrevColumn());
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
