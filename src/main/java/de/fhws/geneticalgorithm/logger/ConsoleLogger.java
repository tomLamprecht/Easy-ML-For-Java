package de.fhws.geneticalgorithm.logger;

import de.fhws.geneticalgorithm.Population;
import de.fhws.geneticalgorithm.Individual;

public class ConsoleLogger implements Logger {

	@Override
	public void log(int maxGen, Population<? extends Individual> population) {
		System.out.println("Gen: " + population.getGeneration() + " / " + maxGen + " best Fitness " + population.getBest().getFitness()  + " avg Fitness: " + population.getAverageFitness());
	}
	
}
