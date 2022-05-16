package de.fhws.geneticalgorithm.selector;

import de.fhws.geneticalgorithm.Population;
import de.fhws.geneticalgorithm.Individual;

import java.util.*;

public class RouletteWheelSelector<T extends Individual<T>> extends PercentageSelector<T> {

    private boolean ensureAddFirst;

    public RouletteWheelSelector(double percent, boolean ensureAddFirst) {
        super(percent);
        this.ensureAddFirst = ensureAddFirst;
    }

    @Override
    public void select(Population<T> pop) {
        double totalFitness = calcTotalFitness(pop);

        List<Double> probabilityList = calcProbabilityList(pop, totalFitness);

        List<Individual<T>> repopulated = repopulate(pop, probabilityList);

        pop.replaceAllIndividuals(repopulated);

    }


    private double calcTotalFitness(Population<T> pop) {
        double totalFitness = 0;
        for (Individual<T> individual : pop.getIndividuals())
            totalFitness += individual.getFitness();
        return totalFitness;
    }

    private List<Double> calcProbabilityList(Population<T> pop, double totalFitness) {
        List<Double> probabilityList = new ArrayList<>(pop.getIndividuals().size());
        double cumulativeProb = 0;
        for(Individual<T> individual : pop.getIndividuals()) {
            if(individual.getFitness() == 0)
                continue;
            cumulativeProb += individual.getFitness() / totalFitness;
            probabilityList.add(cumulativeProb);
        }
        return probabilityList;
    }

    private List<Individual<T>> repopulate(Population<T> pop, List<Double> probabilityList) {
        int goalSize = super.calcGoalSize(pop.getSize());

        List<Individual<T>> repopulated = new ArrayList<>(goalSize);

        if(ensureAddFirst)
            repopulated.add(pop.getBest());

        while (repopulated.size() < goalSize) {
            double bin = Math.random();
            int index = Collections.binarySearch(probabilityList, bin);
            repopulated.add(pop.getIndividuals().get(index >= 0 ? index : -index - 1));
        }

        return repopulated;
    }

}
