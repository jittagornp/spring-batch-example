package me.jittagornp.example.config;

import me.jittagornp.example.listener.JobCompletionListener;
import me.jittagornp.example.processor.SimpleItemProcessor;
import me.jittagornp.example.reader.SimpleItemReader;
import me.jittagornp.example.writer.SimpleItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.SimplePartitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "job", value = "mode", havingValue = "PARTITIONER")
public class Ex09_PartitionerJobConfig {

    private final JobProperties jobProperties;

    private final SimpleItemReader simpleItemReader;

    private final SimpleItemProcessor simpleItemProcessor;

    private final SimpleItemWriter simpleItemWriter;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Bean
    public Job simpleJob(
            JobRepository jobRepository,
            @Qualifier("masterStep") Step masterStep
    ) {
        return new JobBuilder("Partitioner", jobRepository)
                .preventRestart()
                .start(masterStep)
                .listener(new JobCompletionListener(threadPoolTaskExecutor))
                .build();
    }

    @Bean
    public Step masterStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Partitioner partitioner = new SimplePartitioner();
        return new StepBuilder("Master step", jobRepository)
                .partitioner("Master step", partitioner)
                .gridSize(jobProperties.getGridSize())
                .step(slaveStep(jobRepository, transactionManager))
                .taskExecutor(threadPoolTaskExecutor)
                .build();
    }


    @Bean
    public Step slaveStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Slave step", jobRepository)
                .<Integer, String>chunk(jobProperties.getChunkSize(), transactionManager)
                .reader(simpleItemReader)
                .processor(simpleItemProcessor)
                .writer(simpleItemWriter)
                .build();
    }

}
