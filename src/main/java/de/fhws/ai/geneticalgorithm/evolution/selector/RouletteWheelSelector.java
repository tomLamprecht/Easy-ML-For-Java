package de.fhws.ai.geneticalgorithm.evolution.selector;

import de.fhws.ai.geneticalgorithm.Individual;
import de.fhws.ai.geneticalgorithm.Population;
import de.fhws.ai.networktrainer.NNUniformCrossoverRecombiner;
import de.fhws.ai.utility.MultiThreadHelper;
import de.fhws.ai.utility.throwingintefaces.ThrowingRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class RouletteWheelSelector<T extends Individual<T>> extends PercentageSelector<T> {

    private final boolean ensureAddFirst;

    public RouletteWheelSelector(double percent, boolean ensureAddFirst) {
        super(percent);
        this.ensureAddFirst = ensureAddFirst;
    }

    @Override
    public void select(Population<T> pop, ExecutorService executorService) {
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

    private List<Individual<T>> repopulate(Population<T> pop, List<Double> probabilityList, ExecutorService executorService) {
        int goalSize = super.calcGoalSize(pop.getSize());

        List<Individual<T>> repopulated = new ArrayList<>(goalSize);

        if(ensureAddFirst) {
            repopulated.add(pop.getBest());
        }

        if(executorService != null) {
            return doRepopulateMultiThreaded(pop, probabilityList, executorService, goalSize, repopulated);
        }
        else {
            return doRepopulateSingleThreaded(pop, probabilityList, goalSize, repopulated);
        }
    }

    private List<Individual<T>> doRepopulateSingleThreaded(Population<T> pop, List<Double> probabilityList, int goalSize,
        List<Individual<T>> repopulated) {
        while (repopulated.size() < goalSize) {
            repopulated.add(getElementByProbabilityList(pop, probabilityList));
        }
        return repopulated;
    }

    private List<Individual<T>> doRepopulateMultiThreaded(Population<T> pop, List<Double> probabilityList,
        ExecutorService executorService, int goalSize, List<Individual<T>> repopulated)
    {
        List<Individual<T>> synchronizedRepopulated = Collections.synchronizedList(repopulated);

        List<Callable<Void>> calls = getCallsForRepopulation(pop, probabilityList, goalSize, repopulated, synchronizedRepopulated);

        ThrowingRunnable.unchecked( () -> executorService.invokeAll(calls) ).run();

        return synchronizedRepopulated;
    }

    private List<Callable<Void>> getCallsForRepopulation(Population<T> pop, List<Double> probabilityList, int goalSize,
        List<Individual<T>> repopulated, List<Individual<T>> synchronizedRepopulated)
    {
        List<Callable<Void>> calls = new ArrayList<>();

        int initSize = repopulated.size();

        for (int i = 0; i < goalSize - initSize; i++) {
            calls.add( MultiThreadHelper.transformToCallableVoid(
                () -> synchronizedRepopulated.add(getElementByProbabilityList(pop, probabilityList) )
            ) );
        }

        return calls;
    }

    private Individual<T> getElementByProbabilityList(Population<T> pop, List<Double> probabilityList){
        int index = Collections.binarySearch(probabilityList, Math.random());
        return pop.getIndividuals().get(index >= 0 ? index : -index - 1);
    }

    public static class EnsuredSingleThread<T extends Individual<T>> extends RouletteWheelSelector<T>{

        public EnsuredSingleThread(double percent, boolean ensureAddFirst)
        {
            super(percent, ensureAddFirst);
        }

        @Override public void select(Population<T> pop, ExecutorService executorService)
        {
            super.select(pop, null);
        }
    }

    public static class EnsureSingleThreading<T extends Individual<T>> extends RouletteWheelSelector<T>{

        public EnsureSingleThreading( double percent, boolean ensureAddFirst ) {
            super( percent, ensureAddFirst );
        }

        @Override
        public void select( Population<T> pop, ExecutorService executorService ) {
            super.select( pop, null );
        }
    }

}
