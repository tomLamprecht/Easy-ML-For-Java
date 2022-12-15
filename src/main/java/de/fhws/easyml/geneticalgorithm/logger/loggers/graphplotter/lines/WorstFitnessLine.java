package de.fhws.easyml.geneticalgorithm.logger.loggers.graphplotter.lines;

import de.fhws.easyml.geneticalgorithm.Individual;
import de.fhws.easyml.geneticalgorithm.Population;

import java.util.Comparator;
import java.util.function.Function;

public class WorstFitnessLine extends LineGenerator {
    public WorstFitnessLine() {
        super("Worst Fitness");
    }

    @Override
    protected Function<Population<?>, Double> getConverter() {
        return pop -> pop.getIndividuals().stream().min(Comparator.naturalOrder()).map(Individual::getFitness).orElse(0d);
    }
}
