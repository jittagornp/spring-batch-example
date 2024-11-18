package me.jittagornp.example.listener;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class JobCompletionListener implements JobExecutionListener {

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Job completed");
        } else {
            log.info("Job status is \"{}\"", jobExecution.getStatus());
        }

        if (threadPoolTaskExecutor != null) {
            try {
                threadPoolTaskExecutor.shutdown();
                log.info("Shutdown thread pool successfully");
            } catch (Exception e) {
                log.info("Shutdown thread pool error: {}", e.getMessage(), e);
            }
        }
    }

}
