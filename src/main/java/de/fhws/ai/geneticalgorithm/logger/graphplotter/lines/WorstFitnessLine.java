package de.fhws.ai.geneticalgorithm.logger.graphplotter.lines;

import de.fhws.ai.geneticalgorithm.Individual;
import de.fhws.ai.geneticalgorithm.Population;
import de.fhws.ai.geneticalgorithm.logger.graphplotter.lines.LineGenerator;

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
