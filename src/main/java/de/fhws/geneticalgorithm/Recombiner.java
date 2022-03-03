package de.fhws.geneticalgorithm;

public interface Recombiner <T extends Solution> {

	public void recombine(Population<T> pop, int size);
}
