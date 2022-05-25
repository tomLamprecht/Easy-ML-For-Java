package de.fhws.geneticalgorithm.selector;

import de.fhws.geneticalgorithm.Individual;
import de.fhws.geneticalgorithm.Population;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class RouletteWheelSelectorEnsureSingleThreading<T extends Individual<T>> extends RouletteWheelSelector<T>
{
	public RouletteWheelSelectorEnsureSingleThreading(double percent, boolean ensureAddFirst) {
		super(percent, ensureAddFirst);
	}

	@Override
	public void select(Population<T> pop, Optional<ExecutorService> executorService) {
		super.select(pop, Optional.empty());
	}

}
