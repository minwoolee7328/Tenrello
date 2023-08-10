package com.example.tenrello.card.tasklet;

import com.example.tenrello.card.repository.CardRepository;
import com.example.tenrello.entity.Card;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class testTasklet implements Tasklet {
    private final CardRepository cardRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("=====Start execute======");

        // 카드 리스트를 불러옴
        List<Card> cardList = cardRepository.findAll();

        // 마감시간과 비교할 현재 시간을 불러온다.
        LocalDateTime dateTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String startTime =  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        dateTime = LocalDateTime.parse(startTime,formatter);

        System.out.println("dateTime = " + dateTime);

        // 시간 데이터를 이용해서 현재 시간과 비교해 결과를 나타내보자.
        // 마감시간이 현재 시간보다 과거인지



        for(Card card :cardList){

            boolean result = card.getEndTime().isBefore(dateTime);

            if(result){
                // 마감시간이 지남
                card.updateResult("마감");
            }

        }

        return RepeatStatus.FINISHED;
    }

}
