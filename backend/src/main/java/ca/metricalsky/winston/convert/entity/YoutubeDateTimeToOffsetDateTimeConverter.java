package ca.metricalsky.winston.convert.entity;

import com.google.api.client.util.DateTime;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

import java.time.OffsetDateTime;

@Mapper(componentModel = "spring")
public class YoutubeDateTimeToOffsetDateTimeConverter
        implements Converter<DateTime, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(DateTime source) {
        return OffsetDateTime.parse(source.toString());
    }
}
