package de.fhws.geneticalgorithm;

import de.fhws.geneticalgorithm.logger.Logger;
import de.fhws.geneticalgorithm.selector.Selector;

import java.io.File;
import java.util.Optional;
import java.util.function.IntConsumer;

public class GeneticAlgorithm<T extends Individual<T>> {
	
	private int size;
	private final int maxGens;
	private final Population<T> population;
	private final Selector<T> selector;
	private final Optional<Recombiner<T>> recombiner;
	private final Optional<Mutator<T>> mutator;

	private Optional<IntConsumer> genPreperator;
	private final Optional<IntervalSaver> saver;
	private final Optional<Logger> logger;


	public GeneticAlgorithm(PopulationSupplier<T> popSupplier, int maxGens,  Selector<T> selector, Recombiner<T> recombiner, Mutator<T> mutator, IntConsumer genPreperator, IntervalSaver saver, Logger logger) {
		this.population = popSupplier.get();
		this.maxGens = maxGens + population.getGeneration();
		this.size = population.getSize();
		this.selector = selector;
		this.recombiner = Optional.ofNullable(recombiner);
		this.mutator = Optional.ofNullable(mutator);
		this.genPreperator = Optional.ofNullable(genPreperator);
		this.saver = Optional.ofNullable(saver);
		this.logger = Optional.ofNullable(logger);
	}

	public T solve() {
		genPreperator.ifPresent(c -> c.accept(population.getGeneration()));
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
		genPreperator.ifPresent(c -> c.accept(population.getGeneration()));
		selector.select(population);
		recombiner.ifPresent(r -> r.recombine(population, size));
		size = population.getSize();
		mutator.ifPresent(m -> m.mutate(population));
		population.incGeneration();
	}
	
	
	
	
	
	
	public static class Builder<T extends Individual<T>> {


		private int maxGens;
		private PopulationSupplier<T> popSupplier;
		private Selector<T> selector;
		private Recombiner<T> recombiner;
		private Mutator<T> mutator;

		private IntConsumer genPreperator;
		private IntervalSaver saver;
		private Logger logger;

		public Builder (PopulationSupplier<T> popSupplier, int maxGens, Selector<T> selector) {
			this.selector = selector;
			this.maxGens = maxGens;
			this.popSupplier = popSupplier;
		}

		public Builder<T> withRecombiner(Recombiner<T> recombiner) {
			this.recombiner = recombiner;
			return this;
		}

		public Builder<T> withMutator(Mutator<T> mutator) {
			this.mutator = mutator;
			return this;
		}

		public Builder<T> withGenPreperator(IntConsumer genPreperator) {
			this.genPreperator = genPreperator;
			return this;
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
			return new GeneticAlgorithm<T>( popSupplier, maxGens, selector, recombiner, mutator, genPreperator, saver, logger);
		}
	}

}
