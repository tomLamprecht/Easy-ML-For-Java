package de.fhws.ai.geneticalgorithm.evolution;

import de.fhws.ai.geneticalgorithm.Individual;
import de.fhws.ai.geneticalgorithm.Population;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

@FunctionalInterface
public interface Mutator<T extends Individual<T>> {

	void mutate( Population<T> pop, Optional<ExecutorService> executorService);
	
}
