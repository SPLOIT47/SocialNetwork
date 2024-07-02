package com.annotation;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionLogAspect {

    private Logger getLogger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getTarget().getClass());
    }

    @Before("@annotation(com.annotation.ExecutionLog)")
    public void logBeforeMethod(JoinPoint joinPoint) {
        Logger logger = getLogger(joinPoint);
        logger.info("Executing: " + joinPoint.getSignature().toShortString() + 
                    " with arguments: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "@annotation(com.annotation.ExecutionLog)", returning = "result")
    public void logAfterMethod(JoinPoint joinPoint, Object result) {
        Logger logger = getLogger(joinPoint);
        logger.info("Execution of: " + joinPoint.getSignature().toShortString() + 
                    "completed with result: " + result);
    }

    @Around("@annotation(com.annotation.ExecutionLog)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = getLogger(joinPoint);
        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Exception in: " + joinPoint.getSignature().toShortString() + 
                         " with message: " + throwable.getMessage(), throwable);
            throw throwable;
        }
        long timeTaken = System.currentTimeMillis() - startTime;
        logger.info("Executed: " + joinPoint.getSignature().toShortString() + 
                    " in: " + timeTaken + " ms");
        return result;
    }
}
