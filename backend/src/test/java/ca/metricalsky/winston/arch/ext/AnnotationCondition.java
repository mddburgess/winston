package ca.metricalsky.winston.arch.ext;

import com.tngtech.archunit.base.HasDescription;
import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaEnumConstant;
import com.tngtech.archunit.core.domain.properties.HasSourceCodeLocation;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.apache.commons.lang3.EnumUtils;

public class AnnotationCondition<OWNER extends HasDescription & HasSourceCodeLocation>
        extends ArchCondition<JavaAnnotation<OWNER>> {

    private final String propertyName;
    private Object propertyValue;

    public AnnotationCondition(String propertyName) {
        super("explicitly declare %s", propertyName);
        this.propertyName = propertyName;
    }

    public AnnotationCondition<OWNER> setTo(Object propertyValue) {
        this.propertyValue = propertyValue;
        return this;
    }

    @Override
    public void check(JavaAnnotation<OWNER> item, ConditionEvents events) {
        if (!item.hasExplicitlyDeclaredProperty(propertyName)) {
            events.add(SimpleConditionEvent.violated(item, String.format(
                    "%s has not explicitly declared %s in %s",
                    item.getDescription(), propertyName, item.getOwner().getSourceCodeLocation()
            )));
        } else if (propertyValue != null && !getExplicitlyDeclaredProperty(item, propertyName).equals(propertyValue)) {
            events.add(SimpleConditionEvent.violated(item, String.format(
                    "%s has explicitly declared %s set to %s in %s",
                    item.getDescription(), propertyName, item.getExplicitlyDeclaredProperty(propertyName),
                    item.getOwner().getSourceCodeLocation()
            )));
        }
    }

    private <E extends Enum<E>> Object getExplicitlyDeclaredProperty(JavaAnnotation<OWNER> item, String propertyName) {
        var property = item.getExplicitlyDeclaredProperty(propertyName);
        if (property instanceof JavaEnumConstant enumConstant) {
            try {
                var enumClass = (Class<E>) Class.forName(enumConstant.getDeclaringClass().getName());
                return EnumUtils.getEnum(enumClass, enumConstant.name());
            } catch (ClassNotFoundException ex) {
                return property;
            }
        }
        return property;
    }


    @Override
    public String getDescription() {
        if (propertyValue == null) {
            return super.getDescription();
        }
        return String.format("%s set to %s", super.getDescription(), propertyValue);
    }

}
