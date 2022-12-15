package de.fhws.easyml.geneticalgorithm.logger.loggers;

import de.fhws.easyml.geneticalgorithm.Population;
import de.fhws.easyml.geneticalgorithm.Individual;
import de.fhws.easyml.geneticalgorithm.logger.Logger;

public class IntervalConsoleLogger implements Logger {

    ConsoleLogger logger = new ConsoleLogger();
    final int intervall;
    int counter = 0;
    public IntervalConsoleLogger(int intervall) {
        this.intervall = intervall;
    }

    @Override
    public void log(int maxGen, Population<? extends Individual<?>> population) {
        counter++;
        if(counter >= intervall)
        {
            logger.log(maxGen, population);
            counter = 0;
        }
    }
}
