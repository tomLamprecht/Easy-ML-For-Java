package de.fhws.geneticalgorithm.selector;

import de.fhws.geneticalgorithm.Population;
import de.fhws.geneticalgorithm.Individual;

import java.util.*;

public class RouletteWheelSelector<T extends Individual<T>> extends PercentageSelector<T> {

    public RouletteWheelSelector(double percent) {
        super(percent);
    }

    @Override
    public void select(Population<T> pop) {
        double totalFitness = 0;
        for (Individual<T> individual : pop.getIndividuals())
            totalFitness += individual.getFitness();

        List<Double> propbabilityList = new ArrayList<>(pop.getIndividuals().size());

        double cumulativeProb = 0;
        for(Individual<T> individual : pop.getIndividuals()) {
            if(individual.getFitness() == 0)
                continue;
            cumulativeProb += individual.getFitness() / totalFitness;
            propbabilityList.add(cumulativeProb);
        }

        int goalSize = super.calcGoalSize(pop.getSize());
        List<Individual<T>> newIndividuals = new ArrayList<>(goalSize);
        while (newIndividuals.size() < goalSize) {
            double bin = Math.random();
            int index = Collections.binarySearch(propbabilityList, bin);
            newIndividuals.add(pop.getIndividuals().get(index >= 0 ? index : -index - 1));
        }

        pop.replaceAllIndividuals(newIndividuals);

    }
}
