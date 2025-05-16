package ca.metricalsky.winston.arch;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Repository;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeMainClasses
class RepositoryArchTest {

    private static final String REPOSITORY_PACKAGE = "..winston.repository..";

    @ArchTest
    static final ArchRule repositoryClassNamesEndWithRepository =
            classes().that().areAnnotatedWith(Repository.class)
                    .should().haveSimpleNameEndingWith("Repository");

    @ArchTest
    static final ArchRule repositoryClassesAreInRepositoryPackage =
            classes().that().areAnnotatedWith(Repository.class)
                    .should().resideInAPackage(REPOSITORY_PACKAGE);

    @ArchTest
    static final ArchRule repositoryClassesAreInterfaces =
            classes().that().areAnnotatedWith(Repository.class)
                    .should().beInterfaces();

    @ArchTest
    static final ArchRule nonRepositoryClassNamesDoNotEndWithRepository =
            classes().that().areNotAnnotatedWith(Repository.class)
                    .should().haveSimpleNameNotEndingWith("Repository");

    @ArchTest
    static final ArchRule nonRepositoryClassesAreNotInRepositoryPackage =
            classes().that().areNotAnnotatedWith(Repository.class)
                    .should().resideOutsideOfPackage(REPOSITORY_PACKAGE);
}
