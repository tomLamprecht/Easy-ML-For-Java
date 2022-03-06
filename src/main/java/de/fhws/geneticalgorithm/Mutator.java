package de.fhws.geneticalgorithm;

public interface Mutator <T extends Solution>{

	void mutate(Population<T> pop);
	
}
