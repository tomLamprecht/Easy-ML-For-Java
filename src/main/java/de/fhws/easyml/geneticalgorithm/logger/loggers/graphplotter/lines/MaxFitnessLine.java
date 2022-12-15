package de.fhws.easyml.geneticalgorithm.logger.loggers.graphplotter.lines;

import de.fhws.easyml.geneticalgorithm.Population;

import java.util.function.Function;

public class MaxFitnessLine extends LineGenerator {

    /**
     * Plots a line of the max fitness of each Generation
     */
    public MaxFitnessLine() {
        super("Max Fitness");
    }

    @Override
    protected Function<Population<?>, Double> getConverter() {
        return (pop) -> pop.getBest().getFitness();
    }
}
