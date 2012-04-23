package com.incandescent.lean.experiment.db.relational.jdbc;

import com.incandescent.lean.experiment.Subject;

import java.lang.reflect.Field;

/**
 * A set of utilities for reflecting on Subject instances.
 * @author Brad Neighbors
 */
public class SubjectReflectionUtils {

    /**
     * Sets the field of an instance of a Subject.
     * @param subject the subject
     * @param fieldName the name of the declared field on the Subject class
     * @param fieldValue the value to set the field to
     */
    static void setSubjectField(Subject subject, String fieldName, Object fieldValue) {
        try {
            final Field field = Subject.class.getDeclaredField(fieldName);
            final boolean wasAccessible = field.isAccessible();
            if (!wasAccessible) {
                field.setAccessible(true);
            }
            field.set(subject, fieldValue);
            if (!wasAccessible) {
                field.setAccessible(false);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // we explicitly set accessibility
        }
    }
}