package de.fhws.easyml.geneticalgorithm.evolution;

import de.fhws.easyml.geneticalgorithm.Individual;
import de.fhws.easyml.geneticalgorithm.Population;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutorService;

@FunctionalInterface
public interface Recombiner<T extends Individual<T>> {

	/**
	 * Fills up the Population after the Selection step and may recombine the genes of different parents.
	 * @param pop Population to fill up again
	 * @param goalSize The total size that the Population needs to have after the Recombination step
	 * @param executorService may be null. If not it can be used to implement a multithreaded version
	 */
	void recombine(Population<T> pop, int goalSize, @Nullable ExecutorService executorService);
}
