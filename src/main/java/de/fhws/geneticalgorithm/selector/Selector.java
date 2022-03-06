package de.fhws.geneticalgorithm.selector;

import de.fhws.geneticalgorithm.Population;
import de.fhws.geneticalgorithm.Individual;

public interface Selector {
	
	void select(Population<? extends Individual> pop);
}
