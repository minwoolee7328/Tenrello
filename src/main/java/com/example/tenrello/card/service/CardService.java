package com.example.tenrello.card.service;

import com.example.tenrello.card.dto.CardRequestDto;
import com.example.tenrello.card.dto.CardResponseDto;
import com.example.tenrello.card.repository.CardRepository;
import com.example.tenrello.entity.Card;
import com.example.tenrello.entity.User;
import com.example.tenrello.security.details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
//    private final ColumnEntityRepository columnEntityRepository;

    public CardResponseDto createCard(Long columnId, CardRequestDto requestDto, UserDetailsImpl userDetails) {
        // 해당 컬럼 조회 (해당컬림이 존재하는지 확인 밑 조히)
//        ColumnEntity column = ColumnEntityRepository.findById(id);

        // 사용자 입력값
        String title = requestDto.getTitle();

        // 사용자 정보
        User user = userDetails.getUser();

        //카드 생성
        Card card = new Card(user, columnId, title);

        //테이블 저장
        cardRepository.save(card);

        return new CardResponseDto(card);

    }
}
