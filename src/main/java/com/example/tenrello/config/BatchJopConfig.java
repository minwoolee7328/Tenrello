package com.example.tenrello.config;

import com.example.tenrello.card.repository.CardRepository;
import com.example.tenrello.entity.Card;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchJopConfig {
    private final CardRepository cardRepository;
    @Bean
    public Job job(final JobRepository jobRepository, final Step step) {
        return new JobBuilder("job", jobRepository)
                .start(step)
                .build();
    }
    @Bean
    public Step step(final JobRepository jobRepository,
                     final Tasklet testTasklet,
                     final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step", jobRepository)
                .tasklet(testTasklet, transactionManager)
                .build();
    }

//    private ListItemReader<Card> memberReader() {
//        List<Card> cardList =  cardRepository.findAll();
//        return new ListItemReader<>(cardList);
//    }
//    @Bean
//    public ItemWriter<Object> memberWriter() {
//        return this::loggingAll;
//    }
//    private void loggingAll(final Chunk<?> objects) {
//        objects.getItems().forEach(item -> log.info(item.toString()));
//    }

}
