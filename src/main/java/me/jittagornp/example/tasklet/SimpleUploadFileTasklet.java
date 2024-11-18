package me.jittagornp.example.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleUploadFileTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String fileName = contribution.getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .getString("fileName");

        log.info("Uploading file....\"{}\"", fileName);
        Thread.sleep(1000);
        log.info("Upload file finished");
        return RepeatStatus.FINISHED;
    }

}
