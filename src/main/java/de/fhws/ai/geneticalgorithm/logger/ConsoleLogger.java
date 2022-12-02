package de.fhws.ai.geneticalgorithm.logger;

import de.fhws.ai.geneticalgorithm.Individual;
import de.fhws.ai.geneticalgorithm.Population;

public class ConsoleLogger implements Logger {

	@Override
	public void log(int maxGen, Population<? extends Individual<?>> population) {
		System.out.println("Gen: " + population.getGeneration() + " of " + maxGen + " best Fitness " + population.getBest().getFitness()  + " avg Fitness: " + population.getAverageFitness());
	}
	
}
