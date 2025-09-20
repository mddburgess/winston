package ca.metricalsky.winston.convert.common;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

import java.net.URI;

@Mapper(componentModel = "spring")
public class StringToURIConverter
        implements Converter<String, URI> {

    @Override
    public URI convert(@NonNull String source) {
        return URI.create(source);
    }
}
