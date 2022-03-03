package de.fhws.geneticalgorithm;

public interface Mutator <T extends Solution>{

	public void mutate(Population<T> pop);
	
}
