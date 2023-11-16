package com.moa.search.infrastructure.kafka.batch;

import com.moa.global.common.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Qualifier;
import java.time.LocalDateTime;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/batch-run")
public class BatchTestController {

    private final JobLauncher jobLauncher;

    @Qualifier("myJob")
    private final Job job;


    @Operation(summary = "batch test")
    @GetMapping("")
    public ApiResult<Void> handle() {



        LocalDateTime start = LocalDateTime.now().minusDays(1L).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);


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


        return ApiResult.ofSuccess(null);
    }
}
