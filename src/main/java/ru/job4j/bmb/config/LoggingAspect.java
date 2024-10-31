package ru.job4j.bmb.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    // Определяем Pointcut для всех методов в пакете service
    @Pointcut("execution(* job5j.bmb.services.*.*(..))")
    private void serviceLayer() {

    }

    // Определяем before advice для логирования перед вызовом метода
    @Before("serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Вызов метода: " + joinPoint.getSignature().getName());
    }
}
