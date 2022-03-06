package de.fhws.geneticalgorithm;

public interface Mutator <T extends Individual>{

	void mutate(Population<T> pop);
	
}
