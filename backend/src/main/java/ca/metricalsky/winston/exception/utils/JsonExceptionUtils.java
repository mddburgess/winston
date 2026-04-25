package ca.metricalsky.winston.exception.utils;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.FieldError;

import java.util.Collection;
import java.util.regex.Pattern;

public final class JsonExceptionUtils {

    private static final Pattern FIELD_WITH_INDEX = Pattern.compile("(.+)\\[(\\d+)]");

    private JsonExceptionUtils() {

    }

    public static JsonPointer getJsonPointer(FieldError fieldError) {
        if (fieldError == null) {
            return null;
        }

        var fieldElements = fieldError.getField().split("\\.");
        for (var i = 0; i < fieldElements.length; i++) {
            var match = FIELD_WITH_INDEX.matcher(fieldElements[i]);
            if (match.matches()) {
                fieldElements[i] = match.group(1) + "/" + match.group(2);
            }
        }

        return JsonPointer.compile("/" + StringUtils.join(fieldElements, "/"));
    }

    public static JsonPointer getJsonPointer(JsonMappingException exception) {
        if (exception == null) {
            return null;
        }

        var path = exception.getPath();
        if (CollectionUtils.isEmpty(path)) {
            return JsonPointer.empty();
        }

        var pointerElements = path.stream()
                .map(ref -> ref.getIndex() > -1 ? ref.getIndex() : ref.getFieldName())
                .toList();
        return JsonPointer.compile("/" + StringUtils.join(pointerElements, "/"));
    }

    public static String getJsonType(Class<?> javaType) {
        if (javaType == null) {
            return "null";
        }
        if (CharSequence.class.isAssignableFrom(javaType)) {
            return "string";
        }
        if (Number.class.isAssignableFrom(javaType)) {
            return "number";
        }
        if (javaType.isArray() || Collection.class.isAssignableFrom(javaType)) {
            return "array";
        }
        if (Boolean.class.isAssignableFrom(javaType)) {
            return "boolean";
        }
        return "object";
    }
}
