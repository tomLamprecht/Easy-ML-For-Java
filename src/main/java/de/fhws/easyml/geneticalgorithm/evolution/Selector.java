package de.fhws.easyml.geneticalgorithm.evolution;

import de.fhws.easyml.geneticalgorithm.Individual;
import de.fhws.easyml.geneticalgorithm.Population;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutorService;

public interface Selector<T extends Individual<T>> {
	/**
	 * Throws out the worst Individuals of the population
	 * @param pop Population that needs to be selected
	 * @param executorService may be null. If not it can be used to implement a multithreaded version
	 */
	void select(Population<T> pop, @Nullable ExecutorService executorService);
}
