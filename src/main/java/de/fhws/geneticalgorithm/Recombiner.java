package de.fhws.geneticalgorithm;

@FunctionalInterface
public interface Recombiner<T extends Individual<T>> {

	void recombine(Population<T> pop, int goalSize);
}
