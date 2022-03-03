package de.fhws.geneticalgorithm;

public class ConsoleLogger implements Logger{

	@Override
	public void log(int maxGen, Population<? extends Solution> population) {
		System.out.println("Gen: " + population.getGeneration() + " / " + maxGen + " best Fitness " + population.getBest().getFitness()  + " avg Fitness: " + population.getAverageFitness());
	}
	
}
