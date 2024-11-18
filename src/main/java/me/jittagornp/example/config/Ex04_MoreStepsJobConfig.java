package me.jittagornp.example.config;

import me.jittagornp.example.processor.SimpleItemProcessor;
import me.jittagornp.example.reader.SimpleItemReader;
import me.jittagornp.example.tasklet.CleansingFileTasklet;
import me.jittagornp.example.tasklet.DownloadFileTasklet;
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
@ConditionalOnProperty(prefix = "job", value = "mode", havingValue = "MORE_STEPS")
public class Ex04_MoreStepsJobConfig {

    private final JobProperties jobProperties;

    private final SimpleItemReader simpleItemReader;

    private final SimpleItemProcessor simpleItemProcessor;

    private final SimpleItemWriter simpleItemWriter;

    private final DownloadFileTasklet downloadFileTasklet;

    private final CleansingFileTasklet cleansingFileTasklet;

    @Bean
    public Job simpleJob(
            JobRepository jobRepository,
            @Qualifier("downloadFileStep") Step downloadFileStep,
            @Qualifier("processFileStep") Step processFileStep,
            @Qualifier("cleansingFileStep") Step cleansingFileStep
    ) {
        return new JobBuilder("Process file job", jobRepository)
                .preventRestart()
                //3 steps
                .start(downloadFileStep)
                .next(processFileStep)
                .next(cleansingFileStep)
                .build();
    }

    @Bean
    public Step downloadFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Download file step", jobRepository)
                .tasklet(downloadFileTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step processFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Process file step", jobRepository)
                .<Integer, String>chunk(jobProperties.getChunkSize(), transactionManager)
                .reader(simpleItemReader)
                .processor(simpleItemProcessor)
                .writer(simpleItemWriter)
                .build();
    }

    @Bean
    public Step cleansingFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Cleansing file step", jobRepository)
                .tasklet(cleansingFileTasklet, transactionManager)
                .build();
    }

}
