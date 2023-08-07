package com.example.tenrello.card.controller;

import com.example.tenrello.card.dto.CardRequestDto;
import com.example.tenrello.card.dto.CardResponseDto;
import com.example.tenrello.card.service.CardService;
import com.example.tenrello.security.details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card")
@Slf4j(topic = "CardController")
public class cardController {

    private final CardService cardService;

    @PostMapping("/columnId/{columnId}")
    public CardResponseDto createCard(@PathVariable Long columnId, @RequestBody CardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
      return cardService.createCard(columnId, requestDto, userDetails);

    }
}
