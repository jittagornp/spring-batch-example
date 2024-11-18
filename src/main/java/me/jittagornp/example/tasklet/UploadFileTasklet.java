package me.jittagornp.example.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public abstract class UploadFileTasklet implements Tasklet {

    protected abstract void handleUpload(StepContribution contribution, ChunkContext chunkContext) throws Exception;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        handleUpload(contribution, chunkContext);
        return RepeatStatus.FINISHED;
    }

}
