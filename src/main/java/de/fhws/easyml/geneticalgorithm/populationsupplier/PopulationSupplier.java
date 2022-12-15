package de.fhws.easyml.geneticalgorithm.populationsupplier;

import de.fhws.easyml.geneticalgorithm.Population;
import de.fhws.easyml.geneticalgorithm.Individual;

import java.util.function.Supplier;

@FunctionalInterface
public interface PopulationSupplier<T extends Individual<T>> extends Supplier<Population<T>>{

}
