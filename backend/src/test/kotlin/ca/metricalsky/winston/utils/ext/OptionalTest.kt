package ca.metricalsky.winston.utils.ext

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import java.util.Optional

class OptionalTest: WordSpec({

    "orElse()" should {

        "return the Optional value when it is present" {
            Optional.ofNullable("optional") orElse "default" shouldBe "optional"
        }

        "return the default value when the Optional is empty" {
            Optional.empty<String>() orElse "default" shouldBe "default"
        }

        "return the default value when the Optional is null" {
            null as Optional<String>? orElse "default" shouldBe "default"
        }
    }
})
