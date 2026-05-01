package ca.metricalsky.winston.database.test.faker.ext

import net.datafaker.sequence.FakeSequence

fun <T> FakeSequence.Builder<T>.generateList(): List<T> = generate()

fun <T> FakeSequence.Builder<T>.generateSet(): Set<T> = generateList().toSet()
