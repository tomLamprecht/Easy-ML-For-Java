package de.fhws.geneticalgorithm.selector;

import de.fhws.geneticalgorithm.Population;
import de.fhws.geneticalgorithm.Individual;

public class EliteSelector extends PercentageSelector {

    public EliteSelector(double percent) {
        super(percent);
    }

    @Override
    public void select(Population<? extends Individual> pop) {
        pop.sortPopByFitness();
        int goalSize = super.calcGoalSize(pop.getSize());
        while (pop.getSize() > goalSize)
            pop.getIndividuals().remove(goalSize);
    }
}
