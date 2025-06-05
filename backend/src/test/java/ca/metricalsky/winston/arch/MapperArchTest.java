package ca.metricalsky.winston.arch;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.mapstruct.Mapper;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeMainClasses
public class MapperArchTest {

    private static final String MAPPER_PACKAGE = "..winston.mapper..";

    @ArchTest
    private final ArchRule classesNamedAsMappersAreInMapperPackage = classes()
            .that().areTopLevelClasses()
            .and().haveSimpleNameEndingWith("Mapper")
            .should().resideInAPackage(MAPPER_PACKAGE);

    @ArchTest
    private final ArchRule classesNotNamedAsMappersAreNotInMapperPackage = classes()
            .that().areTopLevelClasses()
            .and().haveSimpleNameNotEndingWith("Mapper")
            .and().haveSimpleNameNotEndingWith("MapperImpl")
            .should().resideOutsideOfPackage(MAPPER_PACKAGE);

    @ArchTest
    private final ArchRule classesAnnotatedWithMapperAreNamedAsMappers = classes()
            .that().areAnnotatedWith(Mapper.class)
            .should().haveSimpleNameEndingWith("Mapper");
}
