package edu.java.customaspect.aspects;

import edu.java.customaspect.annotations.SemaphoreRateLimiter;
import edu.java.customaspect.config.CustomAspectConfig;
import edu.java.customaspect.exceptions.RateLimitException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.*;

@Component
@Aspect
@ConditionalOnProperty(prefix = "custom-aspect", name = "rate-limiter.enable", havingValue = "true")
public class SemaphoreRateLimiterAspect {

    private final CustomAspectConfig.RateLimiterConfig config;

    private final Map<String, Semaphore> limiter = new ConcurrentHashMap<>();

    private final static Logger LOGGER = LoggerFactory.getLogger(SemaphoreRateLimiterAspect.class);

    public SemaphoreRateLimiterAspect(CustomAspectConfig config) {
        this.config = config.rateLimiterConfig();
    }

    @Pointcut("@annotation(edu.java.customaspect.annotations.SemaphoreRateLimiter)")
    void annotatedMethod() {
    }

    @Pointcut("@within(edu.java.customaspect.annotations.SemaphoreRateLimiter)")
    void annotatedClass() {
    }

    @Around("annotatedMethod() || annotatedClass()")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        String id = getAnnotationValue(joinPoint);
        Semaphore semaphore = limiter.computeIfAbsent(id, k -> new Semaphore(config.allowed()));
        try {
            boolean isAcquired = semaphore
                    .tryAcquire(config.acquireTimeout().toNanos(), TimeUnit.NANOSECONDS);
            if (isAcquired) {
                return joinPoint.proceed();
            } else {
                throw new RateLimitException(String
                        .format("Rate limit exceeded for rateLimiter: %s", id));
            }
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } finally {
            semaphore.release();
        }
    }

    private String getAnnotationValue(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SemaphoreRateLimiter annotation = method.getAnnotation(SemaphoreRateLimiter.class);
        if (annotation != null) {
            return annotation.value();
        }
        annotation = method.getDeclaringClass().getAnnotation(SemaphoreRateLimiter.class);
        return annotation.value();
    }
}
