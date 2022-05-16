package de.fhws.geneticalgorithm;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

@FunctionalInterface
public interface Mutator<T extends Individual<T>> {

	void mutate(Population<T> pop, Optional<ExecutorService> executorService);
	
}
