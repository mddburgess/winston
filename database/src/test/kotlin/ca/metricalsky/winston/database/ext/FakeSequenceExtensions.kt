package ca.metricalsky.winston.database.ext

import net.datafaker.sequence.FakeSequence

fun <T> FakeSequence.Builder<T>.generateList(): List<T> = generate()
