package de.fhws.geneticalgorithm;

public interface Recombiner <T extends Solution> {

	void recombine(Population<T> pop, int size);
}
