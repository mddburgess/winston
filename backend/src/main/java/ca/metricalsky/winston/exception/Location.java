package ca.metricalsky.winston.exception;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonPointer;

public record Location(
        String pointer,
        Integer line,
        Integer column
) {

    public Location(String pointer) {
        this(pointer, null, null);
    }

    public Location(JsonPointer pointer) {
        this(pointer.toString(), null, null);
    }

    public Location(int line, int column) {
        this(null, line, column);
    }

    public Location(JsonLocation jsonLocation) {
        this(null, jsonLocation.getLineNr(), jsonLocation.getColumnNr());
    }
}
