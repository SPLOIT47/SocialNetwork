package com.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionLogAsect {
  
  @Around("@annotation(com.annotation.ExecutionLog)")
  public Object ExecuionLog(ProceedingJoinPoint joinPoint) throws Throwable {
    Long start = System.currentTimeMillis();
    System.out.println();
    System.out.println("Executing " + joinPoint.getSignature() + "...");
    Object proceed = joinPoint.proceed();
    Long execTime = System.currentTimeMillis() - start;
    System.out.println(joinPoint.getSignature() + " execution time: " + execTime + "ms");
    System.out.println(joinPoint.getSignature() + " input: " + joinPoint.getArgs()[0]);
    System.out.println(joinPoint.getSignature() + " output: " + proceed);
    System.out.println(joinPoint.getSignature() + " executed.");
    System.out.println();
    return proceed;
  }
}
