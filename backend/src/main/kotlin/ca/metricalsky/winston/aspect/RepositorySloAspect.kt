package ca.metricalsky.winston.aspect

import ca.metricalsky.winston.config.properties.RepositoryConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.commons.lang3.time.StopWatch
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Aspect
@Component
class RepositorySloAspect(
    private val repositoryConfig: RepositoryConfig,
) {
    private val log = KotlinLogging.logger {}

    @Pointcut("execution(* ca.metricalsky.winston.database.repository..*.*(..))")
    private fun anyRepositoryMethod() {
    }

    @Around("anyRepositoryMethod()")
    @Throws(Throwable::class)
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val stopwatch = StopWatch.createStarted()

        try {
            return joinPoint.proceed()
        } finally {
            val executionTime = stopwatch.getTime(TimeUnit.MILLISECONDS)

            val methodSignature = joinPoint.signature as MethodSignature
            val sloAnnotation = methodSignature.method.getAnnotation(Slo::class.java)
            val slo = sloAnnotation?.ms ?: repositoryConfig.defaultSloMs

            if (executionTime > slo) {
                log.warn {
                    "${methodSignature.toShortString()} executed in ${executionTime} ms, " +
                            "exceeded SLO of ${slo} ms"
                }
            } else {
                log.debug { "${methodSignature.toShortString()} executed in ${executionTime} ms" }
            }
        }
    }
}
