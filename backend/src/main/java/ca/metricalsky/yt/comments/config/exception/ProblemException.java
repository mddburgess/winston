package ca.metricalsky.yt.comments.config.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@JsonPropertyOrder({"class", "message", "trace", "cause"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemException {

    @JsonProperty("class")
    private String clazz;

    private String message;

    private List<String> trace;

    private ProblemException cause;

    protected ProblemException() {

    }

    public static ProblemException forException(Throwable ex) {
        return forException(ex, List.of());
    }

    public static ProblemException forException(Throwable ex, List<String> parentStackTrace) {
        var stackTrace = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .toList();

        var problemException = new ProblemException();
        problemException.setClazz(ex.getClass().getName());
        problemException.setMessage(ex.getLocalizedMessage());
        problemException.setTrace(truncateStackTrace(stackTrace, parentStackTrace));
        if (ex.getCause() != null) {
            problemException.setCause(ProblemException.forException(ex.getCause(), stackTrace));
        }
        return problemException;
    }

    private static List<String> truncateStackTrace(List<String> stackTrace, List<String> parentStackTrace) {
        var lastIndex = stackTrace.size() - 1;
        var lastParentIndex = parentStackTrace.size() - 1;
        while (lastParentIndex >= 0 && lastIndex >= 0) {
            if (!stackTrace.get(lastIndex).equals(parentStackTrace.get(lastParentIndex))) {
                break;
            }
            lastIndex--;
            lastParentIndex--;
        }

        var truncatedStackTrace = new ArrayList<>(stackTrace.subList(0, lastIndex + 1));
        if (lastIndex < stackTrace.size() - 1) {
            var truncatedLength = stackTrace.size() - 1 - lastIndex;
            truncatedStackTrace.add("... " + truncatedLength + " more");
        }

        return truncatedStackTrace;
    }
}
