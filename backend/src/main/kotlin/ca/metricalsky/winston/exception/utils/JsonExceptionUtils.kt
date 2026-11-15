package ca.metricalsky.winston.exception.utils

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonMappingException
import org.springframework.validation.FieldError

val FieldError.jsonPointer: JsonPointer
    get() = JsonPointer.compile(field.split(".").joinToString(prefix = "/", separator = "/") {
        when (val match = Regex("(.+)\\[(\\d+)]").find(it)) {
            null -> it
            else -> "${match.groupValues[1]}/${match.groupValues[2]}"
        }
    })

val JsonMappingException.jsonPointer: JsonPointer
    get() = when {
        path.isEmpty() -> JsonPointer.empty()
        else -> JsonPointer.compile(path.map {
            when {
                it.index > -1 -> it.index
                else -> it.fieldName
            }
        }.joinToString(prefix = "/", separator = "/"))
    }

val Class<*>?.jsonType: String
    get() = when {
        this == null -> "null"
        CharSequence::class.java.isAssignableFrom(this) -> "string"
        Char::class.java.isAssignableFrom(this) -> "string"
        Boolean::class.java.isAssignableFrom(this) -> "boolean"
        this.isPrimitive || Number::class.java.isAssignableFrom(this) -> "number"
        this.isArray || Collection::class.java.isAssignableFrom(this) -> "array"
        else -> "object"
    }
