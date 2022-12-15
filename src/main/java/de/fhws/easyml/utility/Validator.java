package de.fhws.easyml.utility;

public class Validator {

    public static DoubleValidator value( double value ) {
        return new DoubleValidator( value );
    }

    public static IntValidator value( int value ) {
        return new IntValidator( value );
    }

    public static class DoubleValidator {
        private final double value;

        private DoubleValidator( double value ) {
            this.value = value;
        }

        public boolean isPositive( ) {
            return value > 0;
        }

        public boolean isEqualTo( double other ) {
            return value == other;
        }

        public boolean isBetween( double min, double max ) {
            return value >= min && value <= max;
        }

        public void isPositiveOrThrow( ) {
            isPositiveOrThrow( new IllegalArgumentException( "argument must be positive, but was" + value ) );
        }

        public void isPositiveOrThrow( RuntimeException exception ) {
            if ( !isPositive( ) )
                throw exception;
        }

        public void isEqualToOrThrow( double other ) {
            isEqualToOrThrow( other, new IllegalArgumentException( "argument must be equal to " + other + " but was: " + value ) );
        }

        public void isEqualToOrThrow( double other, RuntimeException exception ) {
            if ( !isEqualTo( other ) )
                throw exception;
        }

        public void isBetweenOrThrow( double min, double max ) {
            isBetweenOrThrow( min, max, new IllegalArgumentException( "argument must be between " + min + " and " + max + " but was: " + value ) );
        }

        public void isBetweenOrThrow( double min, double max, RuntimeException exception ) {
            if ( !isBetween( min, max ) )
                throw exception;
        }
    }

    public static class IntValidator {
        private final int value;

        private IntValidator( int value ) {
            this.value = value;
        }

        public boolean isPositive( ) {
            return value > 0;
        }

        public boolean isEqualTo( int other ) {
            return value == other;
        }

        public boolean isBetween( int min, int max ) {
            return value >= min && value <= max;
        }

        public void isPositiveOrThrow( ) {
            isPositiveOrThrow( new IllegalArgumentException( "argument must be positive, but was" + value ) );
        }

        public void isPositiveOrThrow( RuntimeException exception ) {
            if ( !isPositive( ) )
                throw exception;
        }

        public void isEqualToOrThrow( int other ) {
            isEqualToOrThrow( other, new IllegalArgumentException( "argument must be equal to " + other + " but was: " + value ) );
        }

        public void isEqualToOrThrow( int other, RuntimeException exception ) {
            if ( !isEqualTo( other ) )
                throw exception;
        }

        public void isBetweenOrThrow( int min, int max ) {
            isBetweenOrThrow( min, max, new IllegalArgumentException( "argument must be between " + min + " and " + max + " but was: " + value ) );
        }

        public void isBetweenOrThrow( int min, int max, RuntimeException exception ) {
            if ( !isBetween( min, max ) )
                throw exception;
        }
    }

}
