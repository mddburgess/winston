package ca.metricalsky.winston.test.annotations;

import ca.metricalsky.winston.config.AppResourceResolver;
import ca.metricalsky.winston.convert.ConversionServiceAdapter;
import ca.metricalsky.winston.exception.handlers.ExceptionHandler;
import ca.metricalsky.winston.utils.JsonPatchUtils;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WebMvcTest(includeFilters = {
        @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                AppResourceResolver.class,
                ConversionServiceAdapter.class,
                ExceptionHandler.class,
                JsonPatchUtils.class
        }
)})
public @interface ControllerTest {

    @AliasFor(annotation = WebMvcTest.class)
    Class<?>[] value() default {};
}
