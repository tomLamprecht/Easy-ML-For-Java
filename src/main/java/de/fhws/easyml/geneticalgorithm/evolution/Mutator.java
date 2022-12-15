package de.fhws.easyml.geneticalgorithm.evolution;

import de.fhws.easyml.geneticalgorithm.Individual;
import de.fhws.easyml.geneticalgorithm.Population;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutorService;

@FunctionalInterface
public interface Mutator<T extends Individual<T>> {
	/**
	 * Used to mutate a population.
	 * @param pop Population to be mutated
	 * @param executorService may be null. If not it can be used to implement a multithreaded version
	 */
	void mutate(Population<T> pop, @Nullable ExecutorService executorService);

}
