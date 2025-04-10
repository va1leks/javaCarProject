package com.example.project.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.project.service.*.*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        log.info("Вызов метода: {} с аргументами: {}",
                joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(* com.example.project.service.*.*(..))",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Метод {} выполнен успешно. Результат: {}",
                joinPoint.getSignature(), result);
    }

    @AfterThrowing(pointcut = "execution(* com.example.project.service.*.*(..))",
            throwing = "exception")
    public void logAfterException(JoinPoint joinPoint, Throwable exception) {
        log.error("Ошибка в методе {}: {}",
                joinPoint.getSignature(), exception.getMessage(), exception);
    }

}
