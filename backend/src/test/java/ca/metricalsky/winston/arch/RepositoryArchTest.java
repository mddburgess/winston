package ca.metricalsky.winston.arch;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import static ca.metricalsky.winston.arch.ext.ExtraConditions.explicitlyDeclare;
import static ca.metricalsky.winston.arch.ext.ExtraPredicates.areOfType;
import static ca.metricalsky.winston.arch.ext.ExtraTransformers.methodAnnotations;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.all;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeMainClasses
class RepositoryArchTest {

    private static final String REPOSITORY_PACKAGE = "..winston.repository..";

    @ArchTest
    private final ArchRule repositoryClassNamesEndWithRepository = classes()
            .that().areAnnotatedWith(Repository.class)
            .should().haveSimpleNameEndingWith("Repository");

    @ArchTest
    private final ArchRule repositoryClassesAreInRepositoryPackage = classes()
            .that().areAnnotatedWith(Repository.class)
            .should().resideInAPackage(REPOSITORY_PACKAGE);

    @ArchTest
    private final ArchRule repositoryClassesAreInterfaces = classes()
            .that().areAnnotatedWith(Repository.class)
            .should().beInterfaces();

    @ArchTest
    private final ArchRule nonRepositoryClassNamesDoNotEndWithRepository = classes()
            .that().areNotAnnotatedWith(Repository.class)
            .should().haveSimpleNameNotEndingWith("Repository");

    @ArchTest
    private final ArchRule nonRepositoryClassesAreNotInRepositoryPackage = classes()
            .that().areNotAnnotatedWith(Repository.class)
            .should().resideOutsideOfPackage(REPOSITORY_PACKAGE);

    @ArchTest
    private final ArchRule entityGraphAnnotationsExplicitlyDeclareAttributePaths = all(methodAnnotations())
            .that(areOfType(EntityGraph.class))
            .should(explicitlyDeclare("attributePaths"));
}
