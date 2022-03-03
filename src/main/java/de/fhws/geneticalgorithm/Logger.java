package de.fhws.geneticalgorithm;

public interface Logger {

	public void log(int maxGen, Population<? extends Solution> population);
	
}
