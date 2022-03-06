package de.fhws.geneticalgorithm;

public interface Logger {

	void log(int maxGen, Population<? extends Solution> population);
	
}
