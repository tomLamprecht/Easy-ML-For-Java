package de.fhws.ai.geneticalgorithm.evolution.selector;

import de.fhws.ai.geneticalgorithm.Individual;
import de.fhws.ai.geneticalgorithm.Population;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

public interface Selector<T extends Individual<T>> {
	
	void select(Population<T> pop, ExecutorService executorService);
}
