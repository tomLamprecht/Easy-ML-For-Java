package de.fhws.geneticalgorithm;

import java.util.function.Supplier;

public interface PopulationSupplier<T extends Solution> extends Supplier<Population<T>>{

}
