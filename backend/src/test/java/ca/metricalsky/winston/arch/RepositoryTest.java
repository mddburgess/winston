package ca.metricalsky.winston.arch;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;

import static ca.metricalsky.winston.arch.ArchConstants.APP_CLASSES;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class RepositoryTest {

    private static final String REPOSITORY_PACKAGE = "..winston.repository..";

    @Test
    void repositoryClassNamesEndWithRepository() {
        classes().that().areAnnotatedWith(Repository.class)
                .should().haveSimpleNameEndingWith("Repository")
                .check(APP_CLASSES);
    }

    @Test
    void repositoryClassesAreInRepositoryPackage() {
        classes().that().areAnnotatedWith(Repository.class)
                .should().resideInAPackage(REPOSITORY_PACKAGE)
                .check(APP_CLASSES);
    }

    @Test
    void repositoryClassesAreInterfaces() {
        classes().that().areAnnotatedWith(Repository.class)
                .should().beInterfaces()
                .check(APP_CLASSES);
    }

    @Test
    void nonRepositoryClassNamesDoNotEndWithRepository() {
        classes().that().areNotAnnotatedWith(Repository.class)
                .should().haveSimpleNameNotEndingWith("Repository")
                .check(APP_CLASSES);
    }

    @Test
    void nonRepositoryClassesAreNotInRepositoryPackage() {
        classes().that().areNotAnnotatedWith(Repository.class)
                .should().resideOutsideOfPackage(REPOSITORY_PACKAGE)
                .check(APP_CLASSES);
    }
}
