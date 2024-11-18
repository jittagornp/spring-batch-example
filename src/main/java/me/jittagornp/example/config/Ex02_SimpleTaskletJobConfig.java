package me.jittagornp.example.config;

import me.jittagornp.example.tasklet.SimpleTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "job", value = "mode", havingValue = "SIMPLE_TASKLET")
public class Ex02_SimpleTaskletJobConfig {

    private final SimpleTasklet simpleTasklet;

    @Bean
    public Job simpleJob(
            JobRepository jobRepository,
            Step taskletStep
    ) {
        return new JobBuilder("Simple Tasklet", jobRepository)
                .preventRestart()
                .start(taskletStep)
                .build();
    }

    @Bean
    public Step taskletStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Tasklet step", jobRepository)
                .tasklet(simpleTasklet, transactionManager)
                .build();
    }

}
