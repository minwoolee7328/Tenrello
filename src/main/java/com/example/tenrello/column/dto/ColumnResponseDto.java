package com.example.tenrello.column.dto;

import com.example.tenrello.board.dto.BoardResponseDto;
import com.example.tenrello.card.dto.CardResponseDto;
import com.example.tenrello.entity.Board;
import com.example.tenrello.entity.Card;
import com.example.tenrello.entity.ColumnEntity;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class ColumnResponseDto {
    Long id;
    String title;
    BoardResponseDto board;

//    private Long prevColumn;    //Node
//    private Long nextColumn;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CardResponseDto> cardList = new ArrayList<>();
    // position 값으로 정렬 된 카드 값
    private List<CardResponseDto> posisionCardList = new ArrayList<>();
    public ColumnResponseDto(ColumnEntity column){
        this.id = column.getId();
        this.title = column.getTitle();
        this.board = new BoardResponseDto(column.getBoard());
        this.createdAt = column.getCreatedAt();
        this.modifiedAt = column.getModifiedAt();
        setCardList(column.getCardList());
        setposisionCardList(column.getCardList());
    }

    public void setCardList(List<Card> cardList){
        for(Card card:cardList){
            this.cardList.add(new CardResponseDto(card));
        }
    }

    public void setposisionCardList(List<Card> cardList){
        // cardList 의 값을 position 값으로 정렬
        Collections.sort(cardList, new FruitPriceComparator());
        for(Card card:cardList){
            this.posisionCardList.add(new CardResponseDto(card));
        }
    }

}
// position 을기준으로 card 리스트 정렬
class FruitPriceComparator implements Comparator<Card> {
    @Override
    public int compare(Card f1, Card f2) {
        if (f1.getPosition() > f2.getPosition()) {
            return 1;
        } else if (f1.getPosition() < f2.getPosition()) {
            return -1;
        }
        return 0;
    }
}