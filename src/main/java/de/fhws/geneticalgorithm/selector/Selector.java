package de.fhws.geneticalgorithm.selector;

import de.fhws.geneticalgorithm.Population;
import de.fhws.geneticalgorithm.Individual;

public interface Selector<T extends Individual<T>> {
	
	void select(Population<T> pop);
}
