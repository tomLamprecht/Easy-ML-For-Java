package de.fhws.easyml.geneticalgorithm.logger.loggers;

import de.fhws.easyml.geneticalgorithm.Population;
import de.fhws.easyml.geneticalgorithm.Individual;
import de.fhws.easyml.geneticalgorithm.logger.Logger;

public class ConsoleLogger implements Logger {

	@Override
	public void log(int maxGen, Population<? extends Individual<?>> population) {
		System.out.println("Gen: " + population.getGeneration() + " of " + maxGen + " best Fitness " + population.getBest().getFitness()  + " avg Fitness: " + population.getAverageFitness());
	}
	
}
