package com.incandescent.lean.experiment.db.relational.jdbc;

import com.incandescent.lean.experiment.Option;

import java.lang.reflect.Field;

/**
 * A set of utilities for reflecting on Option classes.
 * @author Brad Neighbors
 */
public class OptionReflectionUtils {

    /**
     * Sets a field on an instance of Option.
     * @param option the option
     * @param fieldName the name of the declared field on the Option class
     * @param fieldValue the value to set the field to
     */
    static void setOptionField(Option option, String fieldName, Object fieldValue) {
        try {
            final Field field = Option.class.getDeclaredField(fieldName);
            final boolean wasAccessible = field.isAccessible();
            if (!wasAccessible) {
                field.setAccessible(true);
            }
            field.set(option, fieldValue);
            if (!wasAccessible) {
                field.setAccessible(false);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // we explicitly set accessibility
        }
    }

    /**
     * Gets the database primary key of an Option instance.
     * @param option the option
     * @return The database primary key ID.
     */
    static Integer getOptionId(Option option) {
        try {
            final Field field = Option.class.getDeclaredField("id");
            final boolean wasAccessible = field.isAccessible();
            if (!wasAccessible) {
                field.setAccessible(true);
            }

            Object id = field.get(option);

            if (!wasAccessible) {
                field.setAccessible(false);
            }

            if (id != null) {
                return (Integer) id;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // we explicitly set accessibility
        }
        return null;
    }

    /**
     * Gets the option's sequence.
     * @param option the option
     * @return the order of the option in the experiment
     */
    static Integer getOptionSequence(Option option) {
        try {
            final Field field = Option.class.getDeclaredField("sequence");
            final boolean wasAccessible = field.isAccessible();
            if (!wasAccessible) {
                field.setAccessible(true);
            }

            Object sequence = field.get(option);

            if (!wasAccessible) {
                field.setAccessible(false);
            }

            if (sequence != null) {
                return (Integer) sequence;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // we explicitly set accessibility
        }
        return null;
    }

}
