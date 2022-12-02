package de.fhws.ai.geneticalgorithm.evolution.selector;

import de.fhws.ai.geneticalgorithm.Individual;
import de.fhws.ai.geneticalgorithm.Population;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class EliteSelector<T extends Individual<T>> extends PercentageSelector<T> {

    public EliteSelector(double percent) {
        super(percent);
    }

    @Override
    public void select(Population<T> pop, Optional<ExecutorService> executorService) {
        pop.sortPopByFitness();
        repopulate(pop);
    }

    private void repopulate(Population<T> pop) {
        int goalSize = super.calcGoalSize(pop.getSize());
        while (pop.getSize() > goalSize)
            pop.getIndividuals().remove(goalSize);
    }

}
