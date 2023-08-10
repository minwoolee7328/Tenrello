package com.example.tenrello.card.service;

import com.example.tenrello.card.dto.CardRequestDto;
import com.example.tenrello.card.dto.CardResponseDto;
import com.example.tenrello.card.dto.CardTimeRequestDto;
import com.example.tenrello.card.dto.CardTimeResponseDto;
import com.example.tenrello.card.repository.CardRepository;
import com.example.tenrello.column.repository.ColumnRepository;
import com.example.tenrello.entity.Card;
import com.example.tenrello.entity.ColumnEntity;
import com.example.tenrello.entity.User;
import com.example.tenrello.security.details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final ColumnRepository columnRepository;

    @Transactional
    public CardResponseDto createCard(Long columnId, CardRequestDto requestDto, UserDetailsImpl userDetails) {
        // 해당 컬럼 조회 (해당컬림이 존재하는지 확인 밑 조히)
//        ColumnEntity column = ColumnEntityRepository.findById(id);

        // 사용자 입력값
        String title = requestDto.getTitle();

        // 사용자 정보
        User user = userDetails.getUser();

        //컬럼 정보

        // position값 생성
        // 각 컬럼에 해당하는 포지션값을 가져야함
        // ex) a 123 , b 123
        // 컬럼 id 필요 / 컬럼의 최종 포지션값 필요

        // 컬럼의 최종 포지션값
        List<Card> cardS = cardRepository.findAllByColumn_id(columnId);

        Optional<ColumnEntity> column = columnRepository.findById(columnId);

        //카드 생성
        Card card = new Card(user, column.get(), title,cardS.size()+1);

        cardRepository.save(card);

        return new CardResponseDto(card);

    }

    // 카드 제목 변경
    @Transactional
    public CardResponseDto updateCardTitle(Long id, CardRequestDto requestDto) {
        // 카드가 존재하는지 확인
        Optional<Card> card = cardRepository.findById(id);

        if(!card.isPresent()){
            throw new IllegalArgumentException("해당 카드가 존재하지 않습니다.");
        }

        String content = requestDto.getTitle();

        card.get().updateTitle(content);

        return new CardResponseDto(card.get());

    }

    // 카드 내용변경
    @Transactional
    public CardResponseDto updateCardContent(Long id, CardRequestDto requestDto) {
        // 카드가 존재하는지 확인
        Optional<Card> card = cardRepository.findById(id);

        if(!card.isPresent()){
            throw new IllegalArgumentException("해당 카드가 존재하지 않습니다.");
        }

        String content = requestDto.getContent();

        card.get().updateContent(content);

        return new CardResponseDto(card.get());
    }

    //카드 삭제
    @Transactional
    public void deleteCard(Long id, UserDetailsImpl userDetails) {
        // 카드가 존재하는지 확인
        Optional<Card> card = cardRepository.findById(id);

        if(!card.isPresent()){
            throw new IllegalArgumentException("해당 카드가 존재하지 않습니다.");
        }

        //보더 생성자인지 확인 (미구현)

        //카드 삭제전 position 값 변경
        // 해당하는 컬럼
        List<Card> cardList = cardRepository.findAllByColumn_id(card.get().getColumn().getId());

        for(int i = card.get().getPosition()-1; i<cardList.size(); i++){
            cardList.get(i).updatePosition(cardList.get(i).getPosition()-1);
        }

        cardRepository.delete(card.get());

    }

    // 카드 컬럼내 변경
    // 변경될 컬럼명 추가
    @Transactional
    public CardResponseDto chId(Long id, CardRequestDto requestDto) {
        // 카드가 존재하는지 확인
        // 선택한 카드
        Optional<Card> card = cardRepository.findById(id);

        if(!card.isPresent()){
            throw new IllegalArgumentException("해당 카드가 존재하지 않습니다.");
        }

        // 도착할 카드의 모든값을 가져옴
        List<Card> cardList = cardRepository.findAllByColumn_id(requestDto.getColumnId());

        // 움직일 번호 (어떤 컬럼의 카드인지)
        int ids = card.get().getPosition();
//        System.out.println("ids = " + ids);

        // 도착할 장소
        int requestDtos = Math.toIntExact(requestDto.getPosition());
//        System.out.println("requestDtos = " + requestDtos);

        // 같은 컬럼내 이동
        if(card.get().getColumn().getId().equals(requestDto.getColumnId())){

            // ex) 2 < 4
            //id 가  requestDto 보다 작을때
            if(ids < requestDtos){
                String temp = "";
                for(int i = ids-1; i<requestDtos-1;i++){
                    temp = cardList.get(i).getTitle();
                    cardList.get(i).updateTitle(cardList.get(i+1).getTitle());
                    cardList.get(i+1).updateTitle(temp);
                }
            }

            // ex)  4 > 1
            //id 가 requestDto 보다 클때
            if(ids > requestDtos){
                String temp = "";
                for(int i = ids-1; i>requestDtos-1;i--){
                    temp = cardList.get(i).getTitle();
                    cardList.get(i).updateTitle(cardList.get(i-1).getTitle());
                    cardList.get(i-1).updateTitle(temp);
                }
            }

            return new CardResponseDto(cardList);
        }else{

            // 현재 id 의 값의 컬럼과
            // 도착할 컬럼의 id 가 다를경우

            // 기존 카드 컬럼 id
            Long col = card.get().getColumn().getId();
            int cardPositionTemp = card.get().getPosition();

            // 선택한 컬럼으로 카드 이동 (기존데이터의 컬럼 id 변경)
            // 컴럼 과 병합시 변경 가능성 있음

            //컬럼아이디

            Optional<ColumnEntity> column = columnRepository.findById(col);

            card.get().updateColumnId(column.get());
            card.get().updatePosition(Math.toIntExact(requestDto.getPosition()));
            cardList.add(card.get());

            String titleTemp = "";
            int positionTemp = 0;

            // 선택한 컬럼 정렬
            for(int i = cardList.size()-1; i>=requestDto.getPosition(); i--){
                System.out.println("\"동작\" = " + "동작");
                titleTemp = cardList.get(i).getTitle();
                positionTemp = cardList.get(i).getPosition();

                cardList.get(i).updateTitle(cardList.get(i-1).getTitle());
                cardList.get(i).updatePosition(cardList.get(i-1).getPosition()+1);

                cardList.get(i-1).updateTitle(titleTemp);
                cardList.get(i-1).updatePosition(positionTemp);
            }

            // 기존 컬럼에 속하는 카드들
            List<Card> cards = cardRepository.findAllByColumn_id(col);

            for(Card cardss:cards){
                System.out.println("cardss = " + cardss.getPosition());
            }

            // 기존 컬럼 데이터 정렬
            // ex> 0 <= 1

            for(int i = cardPositionTemp-1;  i<=cards.size()-1;i++){
                    cards.get(i).updatePosition(cards.get(i).getPosition()-1);
            }

            return new CardResponseDto(cardList);
        }

    }

    // 특정 카드 조회
    public CardResponseDto getCard(Long id) {
        // 카드가 존재하는지 확인
        // 선택한 카드
        Optional<Card> card = cardRepository.findById(id);

        if(!card.isPresent()){
            throw new IllegalArgumentException("해당 카드가 존재하지 않습니다.");
        }

        // 카드 조회시 시간 정보는 startTime 과 endTime 의 데이터를 이용해 보여줄 형식 지정 (미구현)
        // 시작데이터가 있을시 시작시간 ~ 마각시간
        // 시작데이터가 없을시 마감시간만 표시
        // 프론트에서 해결

        return new CardResponseDto(card.get());
    }

    // 시간 데이터 저장
    @Transactional
    public CardTimeResponseDto createTime(Long id, CardTimeRequestDto timeRequestDto) {

        // 카드가 존재하는지 확인
        // 선택한 카드
        Optional<Card> card = cardRepository.findById(id);

        if(!card.isPresent()){
            throw new IllegalArgumentException("해당 카드가 존재하지 않습니다.");
        }
        System.out.println("timeRequestDto.getEndTime() = " + timeRequestDto.getEndTime());
        // 시작 날짜 는 생성할때 시간으로 고정 (시작날짜를 선택했는지 여부)
        if(timeRequestDto.isStartTime()){
            // 시작 날짜와 마감날짜의 데이터를 저장
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            // 시작 날짜 저장
            String startTime =  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime startLocalTime = LocalDateTime.parse(startTime,formatter);

            card.get().updateStartTime(startLocalTime);

            // 사용자에게 받아온 시간 데이터를 변환
            LocalDateTime endLocalTime = LocalDateTime.parse(timeRequestDto.getEndTime(), formatter);

            // 마감시간 저장
            card.get().updateEndTime(endLocalTime);

            // 시간을 저장할때 이미 과거시간을 넣으면 result에 마감이라고 넣기
            boolean result = endLocalTime.isBefore(startLocalTime);

            if(result){
                // 마감시간이 지남
                card.get().updateResult("마감");
            }else{
                card.get().updateResult("진행중");
            }

            return new CardTimeResponseDto(startLocalTime,endLocalTime);
        }

        // 시작날짜가 선택 안되었을때
        // 마감날짜의 데이터를 저장
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String startTime =  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime startLocalTime = LocalDateTime.parse(startTime,formatter);

        // 사용자에게 받아온 시간 데이터를 변환
        LocalDateTime endLocalTime = LocalDateTime.parse(timeRequestDto.getEndTime(), formatter);

        // 마감시간 저장
        card.get().updateEndTime(endLocalTime);

        // 시간을 저장할때 이미 과거시간을 넣으면 result에 마감이라고 넣기
        boolean result = startLocalTime.isBefore(endLocalTime);

        if(result){
            // 마감시간이 지남
            card.get().updateResult("마감");
        }else{
            card.get().updateResult("진행중");
        }

        return new CardTimeResponseDto(endLocalTime);
    }

}