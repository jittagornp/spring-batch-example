package me.jittagornp.example.config;

import me.jittagornp.example.processor.SimpleItemProcessor;
import me.jittagornp.example.reader.SimpleItemReader;
import me.jittagornp.example.tasklet.SimpleTasklet;
import me.jittagornp.example.writer.SimpleItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "job", value = "mode", havingValue = "SIMPLE_CHUNK_ORIENTED_PROCESSING_AND_TASKLET")
public class Ex03_SimpleChunkOrientedProcessingAndTaskletJobConfig {

    private final JobProperties jobProperties;

    private final SimpleItemReader simpleItemReader;

    private final SimpleItemProcessor simpleItemProcessor;

    private final SimpleItemWriter simpleItemWriter;

    private final SimpleTasklet simpleTasklet;

    @Bean
    public Job simpleJob(
            JobRepository jobRepository,
            @Qualifier("simpleStep") Step simpleStep,
            @Qualifier("taskletStep") Step taskletStep
    ) {
        return new JobBuilder("Simple Chunk-oriented Processing and Tasklet", jobRepository)
                .preventRestart()
                .start(simpleStep)
                .next(taskletStep)
                .build();
    }

    @Bean
    public Step simpleStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Simple step", jobRepository)
                .<Integer, String>chunk(jobProperties.getChunkSize(), transactionManager)
                .reader(simpleItemReader)
                .processor(simpleItemProcessor)
                .writer(simpleItemWriter)
                .build();
    }

    @Bean
    public Step taskletStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Tasklet step", jobRepository)
                .tasklet(simpleTasklet, transactionManager)
                .build();
    }

}
