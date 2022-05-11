package de.fhws.geneticalgorithm;

@FunctionalInterface
public interface Mutator<T extends Individual<T>> {

	void mutate(Population<T> pop);
	
}
