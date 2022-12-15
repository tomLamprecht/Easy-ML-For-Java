package de.fhws.easyml.geneticalgorithm.logger;

import de.fhws.easyml.geneticalgorithm.Population;
import de.fhws.easyml.geneticalgorithm.Individual;

public interface Logger {

	void log(int maxGen, Population<? extends Individual<?>> population);
	
}
