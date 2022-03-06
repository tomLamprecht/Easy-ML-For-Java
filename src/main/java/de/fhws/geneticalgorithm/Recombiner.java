package de.fhws.geneticalgorithm;

public interface Recombiner <T extends Individual> {

	void recombine(Population<T> pop, int size);
}
