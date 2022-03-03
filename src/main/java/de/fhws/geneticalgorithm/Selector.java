package de.fhws.geneticalgorithm;

public interface Selector {
	
	public void select(Population<? extends Solution> pop);
}
