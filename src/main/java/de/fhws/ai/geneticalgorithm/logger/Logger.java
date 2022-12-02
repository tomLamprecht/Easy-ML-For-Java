package de.fhws.ai.geneticalgorithm.logger;

import de.fhws.ai.geneticalgorithm.Individual;
import de.fhws.ai.geneticalgorithm.Population;

public interface Logger {

	void log(int maxGen, Population<? extends Individual<?>> population);
	
}
