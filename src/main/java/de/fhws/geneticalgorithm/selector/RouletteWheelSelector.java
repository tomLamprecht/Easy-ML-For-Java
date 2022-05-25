package de.fhws.geneticalgorithm.selector;

import de.fhws.geneticalgorithm.Population;
import de.fhws.geneticalgorithm.Individual;
import de.fhws.utility.MultiThreadHelper;
import de.fhws.utility.throwingintefaces.ThrowingRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class RouletteWheelSelector<T extends Individual<T>> extends PercentageSelector<T> {

    private boolean ensureAddFirst;

    public RouletteWheelSelector(double percent, boolean ensureAddFirst) {
        super(percent);
        this.ensureAddFirst = ensureAddFirst;
    }

    @Override
    public void select(Population<T> pop, Optional<ExecutorService> executorService) {
        double totalFitness = calcTotalFitness(pop);

        List<Double> probabilityList = calcProbabilityList(pop, totalFitness);

        List<Individual<T>> repopulated = repopulate(pop, probabilityList, executorService);

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

    private List<Individual<T>> repopulate(Population<T> pop, List<Double> probabilityList, Optional<ExecutorService> executorService) {
        int goalSize = super.calcGoalSize(pop.getSize());

        List<Individual<T>> repopulated = new ArrayList<>(goalSize);

        if(ensureAddFirst) {
            repopulated.add(pop.getBest());
        }

        if(executorService.isPresent()) {
            return doRepopulateMultiThreaded(pop, probabilityList, executorService.get(), goalSize, repopulated);
        }
        else {
            return doRepopulateSingleThreaded(pop, probabilityList, goalSize, repopulated);
        }
    }

    @NotNull private List<Individual<T>> doRepopulateSingleThreaded(Population<T> pop, List<Double> probabilityList, int goalSize,
        List<Individual<T>> repopulated) {
        while (repopulated.size() < goalSize) {
            repopulated.add(getElementByProbabilityList(pop, probabilityList));
        }
        return repopulated;
    }

    @NotNull private List<Individual<T>> doRepopulateMultiThreaded(Population<T> pop, List<Double> probabilityList,
        ExecutorService executorService, int goalSize, List<Individual<T>> repopulated)
    {
        List<Individual<T>> synchronizedRepopulated = Collections.synchronizedList(repopulated);
        List<Callable<Void>> calls = new ArrayList<>();

        int initSize = repopulated.size();

        for (int i = 0; i < goalSize - initSize; i++) {
            calls.add( MultiThreadHelper.transformToCallableVoid(
                () -> synchronizedRepopulated.add(getElementByProbabilityList( pop, probabilityList) )
            ) );
        }

       ThrowingRunnable.unchecked( () -> executorService.invokeAll(calls) ).run();

        return synchronizedRepopulated;
    }

    private Individual<T> getElementByProbabilityList(Population<T> pop, List<Double> probabilityList){
        int index = Collections.binarySearch(probabilityList, Math.random());
        return pop.getIndividuals().get(index >= 0 ? index : -index - 1);
    }

}
