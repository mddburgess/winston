package ca.metricalsky.winston.exception.utils

import io.kotest.core.spec.style.WordSpec
import io.kotest.datatest.withShoulds
import io.kotest.matchers.shouldBe

class JsonExceptionUtilsTest : WordSpec({

    "Class.jsonType" When {

        withShoulds(
            nameFn = { "for ${it.first?.simpleName} should return '${it.second}'" },
            listOf(
                Pair(null as Class<*>?, "null"),
                Pair(String::class.java, "string"),
                Pair(StringBuilder::class.java, "string"),
                Pair(CharSequence::class.java, "string"),
                Pair(Char::class.java, "string"),
                Pair(Boolean::class.java, "boolean"),
                Pair(Byte::class.java, "number"),
                Pair(Short::class.java, "number"),
                Pair(Int::class.java, "number"),
                Pair(Long::class.java, "number"),
                Pair(Float::class.java, "number"),
                Pair(Double::class.java, "number"),
                Pair(Array<String>::class.java, "array"),
                Pair(List::class.java, "array"),
                Pair(Set::class.java, "array"),
                Pair(Map::class.java, "object"),
                Pair(Any::class.java, "object"),
            ),
        ) { (type, jsonType) ->
            type.jsonType shouldBe jsonType
        }
    }
})
