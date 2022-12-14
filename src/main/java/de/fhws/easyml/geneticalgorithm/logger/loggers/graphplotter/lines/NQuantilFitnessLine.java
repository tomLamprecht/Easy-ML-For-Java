package de.fhws.easyml.geneticalgorithm.logger.loggers.graphplotter.lines;

import de.fhws.easyml.geneticalgorithm.Individual;
import de.fhws.easyml.geneticalgorithm.Population;
import de.fhws.easyml.utility.Validator;

import java.util.function.Function;

public class NQuantilFitnessLine extends LineGenerator {
    final double percentage;

    public NQuantilFitnessLine(double percentage) {
        super((percentage * 100) + "% Quantil");
        Validator.value(percentage).isBetweenOrThrow(0, 1);
        this.percentage = percentage;
    }

    @Override
    protected Function<Population<?>, Double> getConverter() {
        return pop -> pop.getIndividuals()
                        .stream()
                        .sorted()
                        .skip((long) (pop.getSize() * percentage))
                        .findFirst()
                        .map(Individual::getFitness)
                        .orElse(0d);
    }
}
