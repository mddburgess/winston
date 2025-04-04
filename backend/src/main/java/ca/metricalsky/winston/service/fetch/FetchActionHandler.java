package ca.metricalsky.winston.service.fetch;

import ca.metricalsky.winston.entity.fetch.FetchAction;

public interface FetchActionHandler<T> {

    FetchResult<T> fetch(FetchAction fetchAction);
}
