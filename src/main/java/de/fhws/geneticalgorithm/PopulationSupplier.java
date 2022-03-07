package de.fhws.geneticalgorithm;

import java.util.function.Supplier;

public interface PopulationSupplier<T extends Individual<T>> extends Supplier<Population<T>>{

}
