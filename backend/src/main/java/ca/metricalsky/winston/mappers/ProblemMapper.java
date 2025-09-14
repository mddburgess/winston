package ca.metricalsky.winston.mappers;

import ca.metricalsky.winston.api.model.Problem;
import ca.metricalsky.winston.config.exception.AppProblemDetail;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class ProblemMapper implements Converter<Throwable, Problem> {

    @Override
    public Problem convert(Throwable source) {
        var appProblemDetail = AppProblemDetail.forException(source);

        return new Problem()
                .type(appProblemDetail.getType().toString())
                .title(appProblemDetail.getTitle())
                .status(appProblemDetail.getStatus())
                .detail(appProblemDetail.getDetail());
    }
}
