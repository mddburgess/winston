package ca.metricalsky.winston.arch.ext;

import com.tngtech.archunit.core.domain.JavaField;

public final class ExtraConditions {

    private ExtraConditions() {

    }

    public static AnnotationCondition<JavaField> explicitlyDeclare(String propertyName) {
        return new AnnotationCondition<>(propertyName);
    }
}
