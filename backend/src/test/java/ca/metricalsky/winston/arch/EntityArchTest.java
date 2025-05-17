package ca.metricalsky.winston.arch;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Entity;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeMainClasses
class EntityArchTest {

    private static final String ENTITY_PACKAGE = "..winston.entity..";
    private static final String VIEW_PACKAGE = "..winston.entity.view..";

    @ArchTest
    private final ArchRule entityClassNamesEndWithEntity = classes()
            .that().areAnnotatedWith(Entity.class)
            .should().haveSimpleNameEndingWith("Entity");

    @ArchTest
    private final ArchRule entityClassesAreInEntityPackage = classes()
            .that().areAnnotatedWith(Entity.class)
            .should().resideInAPackage(ENTITY_PACKAGE)
            .andShould().resideOutsideOfPackage(VIEW_PACKAGE);

    @ArchTest
    private final ArchRule viewClassNamesEndWithView = classes()
            .that().resideInAPackage(VIEW_PACKAGE)
            .and().areTopLevelClasses()
            .should().haveSimpleNameEndingWith("View");

    @ArchTest
    private final ArchRule viewClassesAreInViewPackage = classes()
            .that().resideInAPackage(ENTITY_PACKAGE)
            .and().areInterfaces()
            .should().resideInAPackage(VIEW_PACKAGE);

    @ArchTest
    private final ArchRule nonEntityClassNamesDoNotEndWithEntity = classes()
            .that().areNotAnnotatedWith(Entity.class)
            .should().haveSimpleNameNotEndingWith("Entity");

    @ArchTest
    private final ArchRule nonEntityClassesAreNotInEntityPackage = classes()
            .that().areTopLevelClasses()
            .and().areNotInterfaces()
            .and().areNotAnnotatedWith(Entity.class)
            .should().resideOutsideOfPackage(ENTITY_PACKAGE);
}
