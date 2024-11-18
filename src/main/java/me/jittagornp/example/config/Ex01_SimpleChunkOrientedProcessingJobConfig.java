package me.jittagornp.example.config;

import me.jittagornp.example.processor.SimpleItemProcessor;
import me.jittagornp.example.reader.SimpleItemReader;
import me.jittagornp.example.writer.SimpleItemWriter;
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
@ConditionalOnProperty(prefix = "job", value = "mode", havingValue = "SIMPLE_CHUNK_ORIENTED_PROCESSING")
public class Ex01_SimpleChunkOrientedProcessingJobConfig {

    private final JobProperties jobProperties;

    private final SimpleItemReader simpleItemReader;

    private final SimpleItemProcessor simpleItemProcessor;

    private final SimpleItemWriter simpleItemWriter;

    @Bean
    public Job simpleJob(
            JobRepository jobRepository,
            Step simpleStep
    ) {
        return new JobBuilder("Simple Chunk-oriented Processing", jobRepository)
                .preventRestart()
                .start(simpleStep)
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

}
