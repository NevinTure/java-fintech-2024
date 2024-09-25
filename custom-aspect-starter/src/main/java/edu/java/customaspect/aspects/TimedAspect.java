package edu.java.customaspect.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TimedAspect {

    private final static Logger log = LoggerFactory.getLogger(TimedAspect.class);

    @Pointcut("@annotation(edu.java.customaspect.annotations.Timed)")
    void annotatedMethod() {
    }

    @Pointcut("@within(edu.java.customaspect.annotations.Timed)")
    void annotatedClass() {
    }

    @Around("annotatedMethod() || annotatedClass()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = getMethodName(signature);
        String className = getClassName(signature);
        log.info(String.format("Method: %s from class: %s started", methodName, className));
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info(String
                .format("Method: %s from class: %s took %d ms to finish",
                        methodName, className, endTime - startTime));
        return result;
    }

    private String getMethodName(MethodSignature signature) {
        return signature.getMethod().getName();
    }

    private String getClassName(MethodSignature signature) {
        return signature.getMethod().getDeclaringClass().getName();
    }
}
