package me.jittagornp.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "job")
public class JobProperties {

    private int chunkSize;

    private int gridSize;

    private int corePoolSize;

    private int maxPoolSize;

    private int queueCapacity;

    private int awaitTerminationSeconds;


}
