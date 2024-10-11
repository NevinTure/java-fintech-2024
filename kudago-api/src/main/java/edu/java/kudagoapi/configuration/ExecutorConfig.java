package edu.java.kudagoapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ExecutorConfig {

    @Bean
    public ExecutorService kudagoUpdateExecutor(@Value("app.pool-size") int poolSize) {
        ThreadFactory threadFactory = new ThreadFactory() {

            private final AtomicInteger threadNum = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("kudago-thread-%d", threadNum.getAndIncrement()));
            }
        };
        return Executors.newFixedThreadPool(poolSize, threadFactory);
    }

    @Bean
    public ScheduledExecutorService kudagoUpdateScheduler() {
        return Executors.newScheduledThreadPool(1);
    }
}
