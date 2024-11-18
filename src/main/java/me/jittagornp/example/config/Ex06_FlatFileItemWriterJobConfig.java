package me.jittagornp.example.config;

import me.jittagornp.example.model.Price;
import me.jittagornp.example.reader.PriceItemReader;
import me.jittagornp.example.tasklet.SimpleUploadFileTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "job", value = "mode", havingValue = "FLAT_FILE_ITEM_WRITER")
public class Ex06_FlatFileItemWriterJobConfig {

    private final JobProperties jobProperties;

    private final PriceItemReader priceItemReader;

    private final SimpleUploadFileTasklet simpleUploadFileTasklet;

    @Bean
    public Job exportPriceReportJob(
            JobRepository jobRepository,
            @Qualifier("exportReportFileStep") Step exportReportFileStep,
            @Qualifier("uploadFileStep") Step uploadFileStep
    ) {
        return new JobBuilder("Export price report job", jobRepository)
                .preventRestart()
                .start(exportReportFileStep)
                .next(uploadFileStep)
                .build();
    }

    @Bean
    public Step exportReportFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        priceItemReader.setPageSize(jobProperties.getChunkSize());
        return new StepBuilder("Export report file step", jobRepository)
                .<Price, Price>chunk(jobProperties.getChunkSize(), transactionManager)
                .reader(priceItemReader)
                .writer(writer(null))
                .build();
    }

    @Bean
    public Step uploadFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Upload file step", jobRepository)
                .tasklet(simpleUploadFileTasklet, transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Price> writer(@Value("#{stepExecution}") StepExecution stepExecution) {
        String fileName = "price.csv";
        stepExecution.getJobExecution().getExecutionContext().put("fileName", fileName);

        return new FlatFileItemWriterBuilder<Price>()
                .name("PriceReportItemWriter")
                .resource(new FileSystemResource(String.format("output/%s", fileName)))
                .delimited()
                .delimiter(",")
                .names("productId", "price")
                .headerCallback(writer -> {
                    writer.write("\uFEFF");
                    writer.write("Product ID,Price");
                })
                .build();
    }

}
