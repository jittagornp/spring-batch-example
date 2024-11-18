package me.jittagornp.example.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "job", value = "upload-type", havingValue = "SFTP")
public class SFTPUploadFileTasklet extends UploadFileTasklet {

    @Override
    protected void handleUpload(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String fileName = contribution.getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .getString("fileName");

        log.info("Uploading file....\"{}\" to SFTP", fileName);
        Thread.sleep(5000);
        log.info("Upload file to SFTP finished");
    }

}
