package ru.job4j.bmb.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* job4j.bmb.services.*.*(..))")
    public void serviceLayer() {
        /*
          А какая в этом месте нужна реализация? Я так понимаю, здесь мы просто обозначаем точку среза и дальнейшие действия обрабатываем согласно
          моему коду - перед выполнением методов, относящихся к моей точке среза (сопоставление происходит по"serviceLayer()" ).
          Я здесь поменяла "private" на "public".
          Не вижу эффекта - где можно посмотреть результаты логирования (что-то еще надо включить для формирования?) И так же не видела текста, когда писала вывод в консоль.
        */
    }

    @Before("serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        LOGGER.info("Before execution method with name: {}", joinPoint.getSignature().getName());
    }
}