package ca.metricalsky.winston.exception;

public record ProblemError(
        String type,
        String detail,
        Location location
) {

    public ProblemError(String type, String detail) {
        this(type, detail, null);
    }
}
