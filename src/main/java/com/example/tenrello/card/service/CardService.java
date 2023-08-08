package com.example.tenrello.card.service;

import com.example.tenrello.card.dto.CardRequestDto;
import com.example.tenrello.card.dto.CardResponseDto;
import com.example.tenrello.card.repository.CardRepository;
import com.example.tenrello.entity.Card;
import com.example.tenrello.entity.User;
import com.example.tenrello.security.details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
//    private final ColumnEntityRepository columnEntityRepository;

    @Transactional
    public CardResponseDto createCard(Long columnId, CardRequestDto requestDto, UserDetailsImpl userDetails) {
        // 해당 컬럼 조회 (해당컬림이 존재하는지 확인 밑 조히)
//        ColumnEntity column = ColumnEntityRepository.findById(id);

        // 사용자 입력값
        String title = requestDto.getTitle();

        // 사용자 정보
        User user = userDetails.getUser();

        // position값 생성
        // 각 컬럼에 해당하는 포지션값을 가져야함
        // ex) a 123 , b 123
        // 컬럼 id 필요 / 컬럼의 최종 포지션값 필요

        // 컬럼의 최종 포지션값
        List<Card> cardS = cardRepository.findAllByColumnid(columnId);

        //카드 생성
        Card card = new Card(user, columnId, title,cardS.size()+1);

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
    public void deleteCard(Long id, UserDetailsImpl userDetails) {
        // 카드가 존재하는지 확인
        Optional<Card> card = cardRepository.findById(id);

        if(!card.isPresent()){
            throw new IllegalArgumentException("해당 카드가 존재하지 않습니다.");
        }

        //보더 생성자인지 확인


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
        List<Card> cardList = cardRepository.findAllByColumnid(requestDto.getColumnId());

        // 움직일 번호 (어떤 컬럼의 카드인지)
        int ids = card.get().getPosition();
        System.out.println("ids = " + ids);

        // 도착할 장소
        int requestDtos = Math.toIntExact(requestDto.getPosition());
        System.out.println("requestDtos = " + requestDtos);

        // 같은 컬럼내 이동
        if(card.get().getColumnid().equals(requestDto.getColumnId())){
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
            Long col = card.get().getColumnid();
            int cardPositionTemp = card.get().getPosition();
            // 선택한 컬럼으로 카드 이동 (기존데이터의 컬럼 id 변경)
            // 컴럼 과 병합시 변경 가능성 있음
            card.get().updateColumnId(requestDto.getColumnId());
            card.get().updatePosition(Math.toIntExact(requestDto.getPosition()));
            cardList.add(card.get());

            String titleTemp = "";
            int positionTemp = 0;
            // 선택한 컬럼 정렬
            // ex) i = 3 ; 3 < 1; i--
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
            List<Card> cards = cardRepository.findAllByColumnid(col);

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

        return new CardResponseDto(card.get());
    }
}
