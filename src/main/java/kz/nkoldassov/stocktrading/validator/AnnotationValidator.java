package kz.nkoldassov.stocktrading.validator;

import kz.nkoldassov.stocktrading.annotation.NotEmpty;
import kz.nkoldassov.stocktrading.annotation.NotNull;
import kz.nkoldassov.stocktrading.annotation.PositivePrimitiveInt;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class AnnotationValidator {

    public static void validate(Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(object);

            validateNotNull(field, value);
            validateNotEmpty(field, value);
            validatePositivePrimitiveInt(object, field);
        }
    }

    private static void validateNotNull(Field field, Object value) {
        if (field.isAnnotationPresent(NotNull.class) && value == null) {
            throw new IllegalArgumentException(
                    field.getAnnotation(NotNull.class).message() + " - " + field.getName()
            );
        }
    }

    private static void validateNotEmpty(Field field, Object value) {
        if (field.isAnnotationPresent(NotEmpty.class) && value instanceof String strValue) {
            if (strValue.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        field.getAnnotation(NotEmpty.class).message() + " - " + field.getName()
                );
            }
        }

        if (field.isAnnotationPresent(NotEmpty.class) && value instanceof Collection<?> collectionValue) {
            if (CollectionUtils.isEmpty(collectionValue)) {
                throw new IllegalArgumentException(
                        field.getAnnotation(NotEmpty.class).message() + " - " + field.getName()
                );
            }
        }

        if (field.isAnnotationPresent(NotEmpty.class) && value instanceof Map<?, ?> mapValue) {
            if (MapUtils.isEmpty(mapValue)) {
                throw new IllegalArgumentException(
                        field.getAnnotation(NotEmpty.class).message() + " - " + field.getName()
                );
            }
        }
    }

    private static void validatePositivePrimitiveInt(Object object, Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(PositivePrimitiveInt.class) && field.getType() == int.class) {
            int intValue = field.getInt(object);
            PositivePrimitiveInt annotation = field.getAnnotation(PositivePrimitiveInt.class);

            if (annotation.includeZero() ? intValue < 0 : intValue <= 0) {
                throw new IllegalArgumentException(
                        annotation.message() + " - " + field.getName()
                );
            }
        }
    }

}