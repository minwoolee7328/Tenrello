package com.example.tenrello.card.controller;

import com.example.tenrello.card.dto.CardRequestDto;
import com.example.tenrello.card.dto.CardResponseDto;
import com.example.tenrello.card.dto.CardTimeRequestDto;
import com.example.tenrello.card.service.CardService;
import com.example.tenrello.common.dto.ApiResponseDto;
import com.example.tenrello.entity.Card;
import com.example.tenrello.security.details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j(topic = "CardController")
public class cardController {

    private final CardService cardService;

    // 카드생성
    @PostMapping("/card/columns/{id}")
    public CardResponseDto createCard(@PathVariable Long id, @RequestBody CardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
      return cardService.createCard(id, requestDto, userDetails);
    }

    // 카드 제목 변경
    @PutMapping("/card/cardTitles/{id}")
    public CardResponseDto updateCardTitle(@PathVariable Long id, @RequestBody CardRequestDto requestDto){
      return cardService.updateCardTitle(id, requestDto);
    }

    // 카드 내용 변경
    @PutMapping("/card/cardContents/{id}")
    public CardResponseDto updateCardContent(@PathVariable Long id, @RequestBody CardRequestDto requestDto){
        return cardService.updateCardContent(id, requestDto);
    }

    //카드 삭제
    @DeleteMapping("/cards/{id}")
    public ResponseEntity<ApiResponseDto> deleteCard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        cardService.deleteCard(id, userDetails);
        return ResponseEntity.status(201).body(new ApiResponseDto("카드 삭제 성공", HttpStatus.OK.value()));
    }

    // 카드 컬럼내 변경
    @PatchMapping("/cards/{id}")
    public CardResponseDto chId(@PathVariable Long id, @RequestBody CardRequestDto requestDto){
        return cardService.chId(id, requestDto);
    }

    // 특정 카드 조회
    @GetMapping("/cards/{id}")
    public CardResponseDto getCard(@PathVariable Long id){
        return cardService.getCard(id);
    }

    // 시간 지정
    @PostMapping("/cards/{id}/time")
    public Card createTime(@PathVariable Long id, @RequestBody CardTimeRequestDto timeRequestDto){
       return cardService.createTime(id, timeRequestDto);
    }

}
