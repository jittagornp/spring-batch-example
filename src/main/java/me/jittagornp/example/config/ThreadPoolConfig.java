package me.jittagornp.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JobProperties.class)
public class ThreadPoolConfig {

    private final JobProperties jobProperties;

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(jobProperties.getCorePoolSize());
        executor.setMaxPoolSize(jobProperties.getMaxPoolSize());
        executor.setQueueCapacity(jobProperties.getQueueCapacity());
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(jobProperties.getAwaitTerminationSeconds());
        executor.initialize();  // Initializes the thread pool
        return executor;
    }

}
