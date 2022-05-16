package de.fhws.geneticalgorithm;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

@FunctionalInterface
public interface Recombiner<T extends Individual<T>> {

	void recombine(Population<T> pop, int goalSize, Optional<ExecutorService> executorService);
}
