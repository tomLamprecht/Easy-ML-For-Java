package de.fhws.ai.geneticalgorithm.evolution.recombiner;

import de.fhws.ai.geneticalgorithm.Individual;
import de.fhws.ai.geneticalgorithm.Population;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

@FunctionalInterface
public interface Recombiner<T extends Individual<T>> {

	/**
	 * Fills up the Population after the Selection step and may recombine the genes of different parents.
	 * @param pop Population to fill up again
	 * @param goalSize The total size that the Population needs to have after the Recombination step
	 * @param executorService may be null. If not it can be used to implement a multithreaded version
	 */
	void recombine( Population<T> pop, int goalSize, @Nullable ExecutorService executorService);
}
