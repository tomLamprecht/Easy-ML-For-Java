package de.fhws.ai.geneticalgorithm.populationsupplier;

import de.fhws.ai.geneticalgorithm.Individual;
import de.fhws.ai.geneticalgorithm.Population;

import java.util.function.Supplier;

@FunctionalInterface
public interface PopulationSupplier<T extends Individual<T>> extends Supplier<Population<T>>{

}
