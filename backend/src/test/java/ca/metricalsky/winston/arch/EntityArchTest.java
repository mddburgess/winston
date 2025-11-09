package ca.metricalsky.winston.arch;

import ca.metricalsky.winston.arch.ext.ExtraConditions;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;

import static ca.metricalsky.winston.arch.ext.ExtraConditions.explicitlyDeclare;
import static ca.metricalsky.winston.arch.ext.ExtraPredicates.areEnums;
import static ca.metricalsky.winston.arch.ext.ExtraPredicates.areOfType;
import static ca.metricalsky.winston.arch.ext.ExtraTransformers.fieldAnnotations;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.all;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

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
            .that().areTopLevelClasses()
            .and().resideInAPackage(VIEW_PACKAGE)
            .should().haveSimpleNameEndingWith("View");

    @ArchTest
    private final ArchRule viewClassesAreInViewPackage = classes()
            .that().areInterfaces()
            .and().resideInAPackage(ENTITY_PACKAGE)
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

    @ArchTest
    private final ArchRule entityFieldsAreAnnotatedWithColumn = fields()
            .that().areDeclaredInClassesThat().areAnnotatedWith(Entity.class)
            .should().beAnnotatedWith(Column.class)
            .orShould().beAnnotatedWith(JoinColumn.class);

    @ArchTest
    private final ArchRule columnAnnotationsExplicitlyDeclareName = all(fieldAnnotations())
            .that(areOfType(Column.class))
            .should(explicitlyDeclare("name"));

    @ArchTest
    private final ArchRule joinColumnAnnotationsExplicitlyDeclareNames = all(fieldAnnotations())
            .that(areOfType(JoinColumn.class))
            .should(ExtraConditions.<JavaField>explicitlyDeclare("name")
                    .and(explicitlyDeclare("referencedColumnName")));

    @ArchTest
    private final ArchRule entityEnumFieldsAreAnnotatedWithEnumerated = fields()
            .that().areDeclaredInClassesThat().areAnnotatedWith(Entity.class)
            .and(areEnums())
            .should().beAnnotatedWith(Enumerated.class);

    @ArchTest
    private final ArchRule enumeratedAnnotationsHaveValueSetToEnumTypeString = all(fieldAnnotations())
            .that(areOfType(Enumerated.class))
            .should(ExtraConditions.<JavaField>explicitlyDeclare("value").setTo(EnumType.STRING));
}
