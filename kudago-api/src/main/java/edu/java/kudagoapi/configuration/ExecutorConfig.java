package edu.java.kudagoapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.*;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "enable-update", havingValue = "true")
public class ExecutorConfig {

    @Bean
    public ThreadPoolTaskExecutor kudagoUpdateExecutor(@Value("${app.pool-size}") int poolSize) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(poolSize);
        executor.setThreadNamePrefix("kudago-thread-");
        executor.setDaemon(true);
        return executor;
    }

    @Bean
    public ScheduledExecutorService kudagoUpdateScheduler() {
        return new ScheduledThreadPoolExecutor(1);
    }
}
