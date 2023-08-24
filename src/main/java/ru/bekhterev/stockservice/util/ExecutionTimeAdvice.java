package ru.bekhterev.stockservice.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnExpression("${aspect.enabled:true}")
@Slf4j
public class ExecutionTimeAdvice {

    @Around("@annotation(ru.bekhterev.stockservice.util.TrackExecutionTime)")
    public Object executionTime(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.nanoTime();
        Object object = point.proceed();
        long endTime = System.nanoTime();
        log.info("Method Name: {}. Thread {}. Time taken for execution is : {} seconds",
                point.getSignature().getName(), Thread.currentThread().getId(),
                (endTime - startTime) / 1_000_000_000.0);
        return object;
    }
}
