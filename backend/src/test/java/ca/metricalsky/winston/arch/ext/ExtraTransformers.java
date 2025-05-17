package ca.metricalsky.winston.arch.ext;

import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.lang.AbstractClassesTransformer;
import com.tngtech.archunit.lang.ClassesTransformer;

import java.util.Collection;

public final class ExtraTransformers {

    private ExtraTransformers() {

    }

    public static ClassesTransformer<JavaAnnotation<JavaField>> fieldAnnotations() {
        return new AbstractClassesTransformer<JavaAnnotation<JavaField>>("field annotations") {
            @Override
            public Iterable<JavaAnnotation<JavaField>> doTransform(JavaClasses collection) {
                return collection.stream()
                        .map(JavaClass::getFields)
                        .flatMap(Collection::stream)
                        .map(JavaField::getAnnotations)
                        .flatMap(Collection::stream)
                        .toList();
            }
        };
    }
}
