package ca.metricalsky.winston.arch;

import jakarta.persistence.Entity;
import org.junit.jupiter.api.Test;

import static ca.metricalsky.winston.arch.ArchConstants.APP_CLASSES;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class EntityTest {

    private static final String ENTITY_PACKAGE = "..winston.entity..";

    @Test
    void entityClassNamesEndWithEntity() {
        classes().that().areAnnotatedWith(Entity.class)
                .should().haveSimpleNameEndingWith("Entity")
                .check(APP_CLASSES);
    }

    @Test
    void entityClassesAreInEntityPackage() {
        classes().that().areAnnotatedWith(Entity.class)
                .should().resideInAPackage(ENTITY_PACKAGE)
                .check(APP_CLASSES);
    }

    @Test
    void nonEntityClassNamesDoNotEndWithEntity() {
        classes().that().areNotAnnotatedWith(Entity.class)
                .should().haveSimpleNameNotEndingWith("Entity")
                .check(APP_CLASSES);
    }

    @Test
    void nonEntityClassesAreNotInEntityPackage() {
        classes().that().areTopLevelClasses()
                .and().areNotAnnotatedWith(Entity.class)
                .should().resideOutsideOfPackage(ENTITY_PACKAGE)
                .check(APP_CLASSES);
    }
}
