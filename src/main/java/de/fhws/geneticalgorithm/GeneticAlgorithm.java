package de.fhws.geneticalgorithm;

import de.fhws.geneticalgorithm.logger.Logger;
import de.fhws.geneticalgorithm.selector.Selector;

import java.io.File;
import java.util.Optional;

public class GeneticAlgorithm<T extends Individual<T>> {
	
	private final int size;
	private final Population<T> population;
	private final Selector<T> selector;
	private final Recombiner<T> recombiner;
	private final Mutator<T> mutator;
	private final int maxGens;
	
	private final Optional<IntervalSaver> saver;
	private final Optional<Logger> logger;
	
	public GeneticAlgorithm(Selector<T> selector, Recombiner<T> recombiner, Mutator<T> mutator, PopulationSupplier<T> popSupplier, int maxGens, Optional<IntervalSaver> saver, Optional<Logger> logger) {
		this.selector = selector;
		this.recombiner = recombiner;
		this.mutator = mutator;
		this.population = popSupplier.get();
		this.size = population.getSize();
		this.maxGens = maxGens + population.getGeneration();
		this.saver = saver;
		this.logger = logger;
	}
	
	public T solve() {
		population.calcFitnesses();
		logger.ifPresent(logger -> logger.log(maxGens, population));
		for(int i = population.getGeneration(); i < maxGens; i++) {
			nextGen();
			population.calcFitnesses();
			saver.ifPresent(saver -> saver.save(population));
			logger.ifPresent(logger -> logger.log(maxGens , population));
		}
		return population.getBest();
	}
	


	private void nextGen() {
		selector.select(population);
		recombiner.recombine(population, size);
		mutator.mutate(population);
		population.incGeneration();
	}
	
	
	
	
	
	
	public static class Builder<T extends Individual<T>> {
		
		private PopulationSupplier<T> popSupplier;
		private Selector<T> selector;
		private Recombiner<T> recombiner;
		private Mutator<T> mutator;
		private int maxGens;
		
		private IntervalSaver saver;
		private Logger logger;
		
		public Builder (Selector<T> selector, Recombiner<T> recombiner, Mutator<T> mutator, PopulationSupplier<T> popSupplier, int maxGens)
		{
			this.selector = selector;
			this.recombiner = recombiner;
			this.mutator = mutator;
			this.maxGens = maxGens;
			this.popSupplier = popSupplier;
		}
		
		/**
		* used to save populations to file
		*
		* @param dir is a directory where the population will be saved in
		* @param interval is the interval of which the populations should be saved
		* @param override determines whether a new file should be created each time or the old one overridden
		* 
		* @return Builder for the Builder pattern
		**/
		public Builder<T> withSaveToFile(File dir, int interval, boolean override){
			this.saver = new IntervalSaver(interval, override, dir);
			return this;
		}
		
		public Builder<T> withLogger(Logger logger){
			this.logger = logger;
			return this;
		}
		
		
		public GeneticAlgorithm<T> build(){
			Optional<IntervalSaver> optSaver = Optional.ofNullable(saver);
			Optional<Logger> optLogger = Optional.ofNullable(logger);
			return new GeneticAlgorithm<>(selector, recombiner, mutator, popSupplier, maxGens, optSaver, optLogger);
		}
	}
	
}
