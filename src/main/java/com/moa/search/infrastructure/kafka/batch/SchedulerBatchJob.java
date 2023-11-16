package com.moa.search.infrastructure.kafka.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class SchedulerBatchJob {


    private final JobLauncher jobLauncher;


    private final Job job;

    @Scheduled(cron = "0 0 24 * * *")
    public void perform() throws JobExecutionException {

//        LocalDateTime start = LocalDateTime.now().minusHours(1L);
        LocalDateTime start = LocalDateTime.now().minusDays(1L).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = LocalDateTime.now();


        JobParameters jobParameters = new JobParametersBuilder()
                .addLocalDateTime("start", start)
                .addLocalDateTime("end", end)
                .toJobParameters();
        try {
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            if (jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
                log.info("jobExecution.getExitStatus() : {}", jobExecution.getExitStatus());

            } else {
                log.info("jobExecution.getExitStatus() : {}", jobExecution.getExitStatus());

            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
