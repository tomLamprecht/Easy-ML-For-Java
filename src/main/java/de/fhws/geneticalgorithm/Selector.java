package de.fhws.geneticalgorithm;

public interface Selector {
	
	void select(Population<? extends Solution> pop);
}
