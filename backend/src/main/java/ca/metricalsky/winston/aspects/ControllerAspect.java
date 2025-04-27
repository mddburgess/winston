package ca.metricalsky.winston.aspects;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class ControllerAspect {

    @Pointcut("execution(* ca.metricalsky.winston.web..*.*(..))")
    private void anyControllerMethod() {}

    @Around("anyControllerMethod()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        var stopwatch = StopWatch.createStarted();
        var returnValue = joinPoint.proceed();
        log.info("Request {} executed in {} ms", joinPoint.getSignature().toShortString(),
                stopwatch.getTime(TimeUnit.MILLISECONDS));
        return returnValue;
    }
}
