package de.fhws.geneticalgorithm.selector;

import de.fhws.geneticalgorithm.Individual;
import de.fhws.geneticalgorithm.Population;

public class EliteSelector<T extends Individual<T>> extends PercentageSelector<T> {

    public EliteSelector(double percent) {
        super(percent);
    }

    @Override
    public void select(Population<T> pop) {
        pop.sortPopByFitness();
        repopulate(pop);
    }

    private void repopulate(Population<T> pop) {
        int goalSize = super.calcGoalSize(pop.getSize());
        while (pop.getSize() > goalSize)
            pop.getIndividuals().remove(goalSize);
    }

}
