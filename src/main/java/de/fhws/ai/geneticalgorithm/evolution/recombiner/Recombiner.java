package de.fhws.ai.geneticalgorithm.evolution.recombiner;

import de.fhws.ai.geneticalgorithm.Individual;
import de.fhws.ai.geneticalgorithm.Population;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

@FunctionalInterface
public interface Recombiner<T extends Individual<T>> {

	void recombine( Population<T> pop, int goalSize, Optional<ExecutorService> executorService);
}
