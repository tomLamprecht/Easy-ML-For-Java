package de.fhws.geneticalgorithm.logger;

import de.fhws.geneticalgorithm.Population;
import de.fhws.geneticalgorithm.Individual;

public interface Logger {

	void log(int maxGen, Population<? extends Individual> population);
	
}
