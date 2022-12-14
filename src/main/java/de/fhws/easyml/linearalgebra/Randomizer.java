package de.fhws.easyml.linearalgebra;

import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {
    private final double min;
    private final double max;

    public Randomizer(double min, double max) {
        if(min > max)
            throw new IllegalArgumentException("min must be smaller than or equal to max");
        this.min = min;
        this.max = max;
    }

    public double getInRange() {
        return min + ThreadLocalRandom.current().nextDouble() * (max - min);
    }
}
