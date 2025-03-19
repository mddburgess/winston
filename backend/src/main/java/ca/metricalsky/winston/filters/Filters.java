package ca.metricalsky.winston.filters;

import org.springframework.core.Ordered;

final class Filters {

    static final int REQUEST_ID = Ordered.HIGHEST_PRECEDENCE;
    static final int CLACKS_OVERHEAD = REQUEST_ID + 1;

    private Filters() {

    }
}
