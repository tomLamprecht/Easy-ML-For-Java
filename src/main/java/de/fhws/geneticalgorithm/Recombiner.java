package de.fhws.geneticalgorithm;

public interface Recombiner<T extends Individual<T>> {

	void recombine(Population<T> pop, int size);
}
