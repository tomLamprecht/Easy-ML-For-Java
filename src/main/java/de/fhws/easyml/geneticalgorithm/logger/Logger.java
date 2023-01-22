package de.fhws.easyml.geneticalgorithm.logger;

import de.fhws.easyml.geneticalgorithm.Population;
import de.fhws.easyml.geneticalgorithm.Individual;
import de.fhws.easyml.logger.LoggerInterface;

public interface Logger extends LoggerInterface<Population<? extends Individual<?>>> {

	@Override
	void log(int maxGen, Population<? extends Individual<?>> population);
	
}
