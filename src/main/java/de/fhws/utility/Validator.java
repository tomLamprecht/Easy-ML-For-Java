package de.fhws.utility;

public class Validator {

    public static void validateBetweenAndThrow(double value, double min, double max) {
        if(validateBetween(value, min, max))
            throw new IllegalArgumentException("value must be between " + min + " and  " + max + " but was: " + value);
    }

    public static boolean validateBetween(double value, double min, double max) {
        return value >= min && value <= max;
    }
}
