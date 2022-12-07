package de.fhws.ai.geneticalgorithm.logger.graphplotter.lines;

import de.fhws.ai.geneticalgorithm.Population;

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
