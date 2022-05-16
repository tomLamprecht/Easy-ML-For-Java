package de.fhws.geneticalgorithm.selector;

import de.fhws.geneticalgorithm.Population;
import de.fhws.geneticalgorithm.Individual;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

public interface Selector<T extends Individual<T>> {
	
	void select(Population<T> pop, Optional<ExecutorService> executorService);
}
