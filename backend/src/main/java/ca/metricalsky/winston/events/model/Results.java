package ca.metricalsky.winston.events.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

import java.util.Collection;
import java.util.Collections;

@Value
@JsonPropertyOrder({"count", "total_count", "items"})
public class Results<T> {

    @JsonProperty("count")
    int count;

    @JsonProperty("total_count")
    int totalCount;

    @JsonProperty("items")
    Collection<T> items;

    public Results(int totalCount, Collection<T> items) {
        this.totalCount = totalCount;
        if (items == null) {
            this.count = 0;
            this.items = Collections.emptyList();
        } else {
            this.count = items.size();
            this.items = items;
        }
    }
}
