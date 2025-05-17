package ca.metricalsky.winston.arch.ext;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.base.HasDescription;
import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.properties.HasSourceCodeLocation;

import java.lang.annotation.Annotation;

public final class ExtraPredicates {

    private ExtraPredicates() {

    }

    public static <T extends HasDescription & HasSourceCodeLocation>
    DescribedPredicate<JavaAnnotation<T>> areOfType(Class<? extends Annotation> annotationType) {
        return new DescribedPredicate<>("are of type @%s", annotationType.getSimpleName()) {
            @Override
            public boolean test(JavaAnnotation<T> javaAnnotation) {
                return javaAnnotation.getRawType().isEquivalentTo(annotationType);
            }
        };
    }

    public static DescribedPredicate<JavaField> areEnums() {
        return new DescribedPredicate<>("are enums") {
            @Override
            public boolean test(JavaField javaField) {
                return javaField.getRawType().isAssignableTo(Enum.class);
            }
        };
    }
}
