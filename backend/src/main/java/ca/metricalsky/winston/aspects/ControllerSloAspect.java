package ca.metricalsky.winston.aspects;

import ca.metricalsky.winston.config.properties.controller.ControllerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ControllerSloAspect {

    private final ControllerConfig controllerConfig;

    @Pointcut("execution(* ca.metricalsky.winston.web..*.*(..))")
    private void anyControllerMethod() {}

    @Around("anyControllerMethod()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        var stopwatch = StopWatch.createStarted();
        var returnValue = joinPoint.proceed();
        var executionTime = stopwatch.getTime(TimeUnit.MILLISECONDS);

        var methodSignature = (MethodSignature) joinPoint.getSignature();
        var sloAnnotation = methodSignature.getMethod().getAnnotation(Slo.class);
        var slo = sloAnnotation != null ? sloAnnotation.ms() : controllerConfig.getDefaultSloMs();

        if (executionTime > slo) {
            log.warn("Request {} executed in {} ms, exceeded SLO of {} ms",
                    joinPoint.getSignature().toShortString(), executionTime, slo);
        } else {
            log.debug("Request {} executed in {} ms",
                    joinPoint.getSignature().toShortString(), executionTime);
        }

        return returnValue;
    }
}
