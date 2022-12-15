package de.fhws.easyml.geneticalgorithm.evolution.selectors;

import de.fhws.easyml.geneticalgorithm.Individual;
import de.fhws.easyml.geneticalgorithm.Population;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutorService;

public class EliteSelector<T extends Individual<T>> extends PercentageSelector<T> {

    public EliteSelector(double percent) {
        super(percent);
    }

    @Override
    public void select(Population<T> pop, @Nullable ExecutorService executorService) {
        pop.sortPopByFitness();
        repopulate(pop);
    }

    private void repopulate(Population<T> pop) {
        int goalSize = super.calcGoalSize(pop.getSize());
        while (pop.getSize() > goalSize)
            pop.getIndividuals().remove(goalSize);
    }

}
