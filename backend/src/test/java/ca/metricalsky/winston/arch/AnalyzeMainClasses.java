package ca.metricalsky.winston.arch;

import ca.metricalsky.winston.Application;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@AnalyzeClasses(packagesOf = Application.class, importOptions = DoNotIncludeTests.class)
public @interface AnalyzeMainClasses {

}
