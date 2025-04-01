package com.sploit.socialnetwork.auth.constraint;

import com.sploit.socialnetwork.auth.annotation.OneNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class AtLeastOneNotBlankConstraint implements ConstraintValidator<OneNotBlank, Object> {
    private String[] fields;

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) {
            return false;
        }

        try {
            for (String fieldName : fields) {
                Field field = object.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(object);
                if (value != null) {
                    return true;
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Validation error: field access issue", e);
        }

        return false;
    }

    @Override
    public void initialize(OneNotBlank constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
    }
}
