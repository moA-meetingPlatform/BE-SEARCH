package com.moa.search.infrastructure.kafka.batch;


import com.moa.search.infrastructure.kafka.KafkaProducer;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.moa.search.domain.QMeeting.meeting;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Batchjob {

    private final static int CHUNK_SIZE = 10;
//    private final static String EVENT_REDIS_KEY_PREFIX = "event:";
    private final EntityManagerFactory entityManagerFactory;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private final JPAQueryFactory queryFactory;

    private final KafkaProducer kafkaProducer;


    @Bean
    public Job myJob() throws Exception {

        return new JobBuilder("myJob", jobRepository)
                .start(testStep(jobRepository, null, null))
                .build();
    }

    @Bean
    @JobScope
    public Step testStep(JobRepository jobRepository, @Value("#{jobParameters['start']}") LocalDateTime start, @Value("#{jobParameters['end']}") LocalDateTime end) throws Exception {
        return new StepBuilder("testStep", jobRepository)
                .<Tuple, String>chunk(CHUNK_SIZE, transactionManager)
                .reader(reader(null, null))
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @Transactional(readOnly = true)
    @StepScope
    public QuerydslPagingItemReader<Tuple> reader( @Value("#{jobParameters['start']}") LocalDateTime start, @Value("#{jobParameters['end']}") LocalDateTime end) {



        return new QuerydslPagingItemReader<>(entityManagerFactory, CHUNK_SIZE, queryFactory -> queryFactory
                .select(meeting.id, meeting.meetingTitle)
                .from(meeting)
                // 시작시간 부터 끝 시간 사이에 있는 meeting을 가져온다.
                .where(meeting.createDatetime.between(start, end)));

    }


    @Bean
    public ItemProcessor<Tuple, String> processor() {
        return tuple -> {
            String res = tuple.get(meeting.id).toString() + ", " + tuple.get(meeting.meetingTitle).toString();;
//            String meetingTitle =

            return res;
        };
    }


    @Bean
    public ItemWriter<String> writer() {
        return chunk -> {
            log.info("write 시작");
            // redisTemplete pipline을 이용해 set으로 저장
            for (String str : chunk.getItems()) {
                log.debug("write : {}", str);
                kafkaProducer.sendMessage(str);

            }
        };
    }
}
