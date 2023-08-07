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

        //카드 생성
        Card card = new Card(user, columnId, title);

        card.updatePosition(cardRepository.save(card).getId());

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

    // 카트 컬럼내 변경
    @Transactional
    public CardResponseDto chId(Long id, CardRequestDto requestDto) {
        // 카드가 존재하는지 확인
        // 선택한 카드
        Optional<Card> card = cardRepository.findById(id);

        if(!card.isPresent()){
            throw new IllegalArgumentException("해당 카드가 존재하지 않습니다.");
        }

        // 카드의 모든값을 가져옴
        List<Card> cardList = cardRepository.findAll();

        // 움직일 번호
        int ids = Math.toIntExact(id);
        // 도착할 장소
        int requestDtos = Math.toIntExact(requestDto.getId());

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

        List<Card> lastCardList = cardRepository.findAll();

        return new CardResponseDto(cardList);

    }
}
