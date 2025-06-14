package ca.metricalsky.winston.arch;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.mapstruct.Mapper;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeMainClasses
public class MapperArchTest {

    @Deprecated
    private static final String MAPPER_PACKAGE = "..winston.mapper..";
    private static final String MAPPERS_PACKAGE = "..winston.mappers..";

    @ArchTest
    private final ArchRule classesNamedAsMappersAreInMappersPackage = classes()
            .that().areTopLevelClasses()
            .and().haveSimpleNameEndingWith("Mapper")
            .should().resideInAPackage(MAPPER_PACKAGE)
            .orShould().resideInAPackage(MAPPERS_PACKAGE);

    @ArchTest
    private final ArchRule classesNotNamedAsMappersAreNotInMappersPackage = classes()
            .that().areTopLevelClasses()
            .and().haveSimpleNameNotEndingWith("Mapper")
            .and().haveSimpleNameNotEndingWith("MapperImpl")
            .should().resideOutsideOfPackage(MAPPER_PACKAGE)
            .andShould().resideOutsideOfPackage(MAPPERS_PACKAGE);

    @ArchTest
    private final ArchRule classesAnnotatedWithMapperAreNamedAsMappers = classes()
            .that().areAnnotatedWith(Mapper.class)
            .should().haveSimpleNameEndingWith("Mapper");
}
