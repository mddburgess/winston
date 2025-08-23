package ca.metricalsky.winston.service.fetch.action;

import ca.metricalsky.winston.entity.fetch.FetchActionEntity;
import ca.metricalsky.winston.service.fetch.FetchResult;

public interface FetchAction<T> {

    FetchResult<T> fetch(FetchActionEntity fetchAction);
}
