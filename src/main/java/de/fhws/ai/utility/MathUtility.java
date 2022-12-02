package de.fhws.ai.utility;

public class MathUtility {

    public static double scaleNumber( double unscaled, double to_min, double to_max, double from_min, double from_max ) {
        return ( to_max - to_min ) * ( unscaled - from_min ) / ( from_max - from_min ) + to_min;
    }

}
