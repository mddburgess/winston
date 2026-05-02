package ca.metricalsky.winston.database.test.jpa.ext

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

fun <E> TestEntityManager.refreshAll(entities: Iterable<E>) = entities.map { this.refresh(it) }
