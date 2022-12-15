package de.fhws.easyml.geneticalgorithm.logger.loggers.graphplotter.lines;

import de.fhws.easyml.geneticalgorithm.Population;

import java.util.function.Function;

public class AvgFitnessLine extends LineGenerator {
    /**
     * Plots a Line of the average fitness of each Generation
     */
    public AvgFitnessLine() {
        super("Avg. Fitness");
    }

    @Override
    protected Function<Population<?>, Double> getConverter() {
        return Population::getAverageFitness;
    }
}
