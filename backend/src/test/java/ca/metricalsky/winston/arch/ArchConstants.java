package ca.metricalsky.winston.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS;

public final class ArchConstants {

    public static final JavaClasses APP_CLASSES = new ClassFileImporter()
            .withImportOption(DO_NOT_INCLUDE_TESTS)
            .importPackages("ca.metricalsky.winston");

    private ArchConstants() {

    }
}
