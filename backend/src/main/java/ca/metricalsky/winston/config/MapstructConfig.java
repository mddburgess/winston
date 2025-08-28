package ca.metricalsky.winston.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.extensions.spring.SpringMapperConfig;

@MapperConfig
@SpringMapperConfig(conversionServiceAdapterPackage = "ca.metricalsky.winston.convert")
public class MapstructConfig {

}
