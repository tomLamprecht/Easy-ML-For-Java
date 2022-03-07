package de.fhws.geneticalgorithm;

public interface Mutator<T extends Individual<T>> {

	void mutate(Population<T> pop);
	
}
