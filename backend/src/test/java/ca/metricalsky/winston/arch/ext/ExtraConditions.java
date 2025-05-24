package ca.metricalsky.winston.arch.ext;

import com.tngtech.archunit.core.domain.JavaMember;

public final class ExtraConditions {

    private ExtraConditions() {

    }

    public static <T extends JavaMember>
    AnnotationCondition<T> explicitlyDeclare(String propertyName) {
        return new AnnotationCondition<>(propertyName);
    }
}
