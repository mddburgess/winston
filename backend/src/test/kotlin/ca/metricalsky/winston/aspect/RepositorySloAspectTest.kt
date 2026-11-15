package ca.metricalsky.winston.aspect

import ca.metricalsky.winston.config.properties.RepositoryConfig
import ch.qos.logback.classic.Level
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.inspectors.forOne
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.reflect.MethodSignature
import org.logcapture.kotest.LogCaptureListener
import java.lang.reflect.Method
import kotlin.time.Duration.Companion.milliseconds

class RepositorySloAspectTest : WordSpec({

    val logger = LogCaptureListener()
    extensions(logger)

    val repositoryConfig = mockk<RepositoryConfig>()
    every { repositoryConfig.defaultSloMs } returns 5

    val repositorySloAspect = RepositorySloAspect(repositoryConfig)

    "logExecutionTime()" should {

        val method = mockk<Method>()

        val methodSignature = mockk<MethodSignature>()
        every { methodSignature.method } returns method
        every { methodSignature.toShortString() } returns "methodSignature"

        val joinPoint = mockk<ProceedingJoinPoint>()
        every { joinPoint.signature } returns methodSignature

        "log a warning when the method takes longer than the default service level" {
            every { joinPoint.proceed() } coAnswers {
                delay(10.milliseconds)
            }
            every { method.getAnnotation(Slo::class.java) } returns null

            repositorySloAspect.logExecutionTime(joinPoint)

            logger.logged().events.forOne {
                it.level shouldBe Level.WARN
                it.message shouldMatch "methodSignature executed in \\d+ ms, exceeded SLO of 5 ms"
            }
        }

        "log a warning when the method takes longer than the custom service level" {
            every { joinPoint.proceed() } coAnswers {
                delay(20.milliseconds)
            }
            every { method.getAnnotation(Slo::class.java) } returns Slo(ms = 10)

            repositorySloAspect.logExecutionTime(joinPoint)

            logger.logged().events.forOne {
                it.level shouldBe Level.WARN
                it.message shouldMatch "methodSignature executed in \\d+ ms, exceeded SLO of 10 ms"
            }
        }

        "log a warning even when the target method throws an exception" {
            every { joinPoint.proceed() } coAnswers {
                delay(10.milliseconds)
                throw RuntimeException()
            }
            every { method.getAnnotation(Slo::class.java) } returns null

            shouldThrow<RuntimeException> {
                repositorySloAspect.logExecutionTime(joinPoint)
            }

            logger.logged().events.forOne {
                it.level shouldBe Level.WARN
                it.message shouldMatch "methodSignature executed in \\d+ ms, exceeded SLO of 5 ms"
            }
        }

        "log a debug message when the method takes less time than the default service level" {
            every { joinPoint.proceed() } returns null
            every { method.getAnnotation(Slo::class.java) } returns null

            repositorySloAspect.logExecutionTime(joinPoint)

            // debug messages aren't captured because the default log level is INFO
            logger.logged().events.shouldBeEmpty()
        }

        "log a debug message when the method takes less time than the custom service level" {
            every { joinPoint.proceed() } coAnswers {
                delay(20.milliseconds)
            }
            every { method.getAnnotation(Slo::class.java) } returns Slo(ms = 100)

            repositorySloAspect.logExecutionTime(joinPoint)

            // debug messages aren't captured because the default log level is INFO
            logger.logged().events.shouldBeEmpty()
        }
    }
})
