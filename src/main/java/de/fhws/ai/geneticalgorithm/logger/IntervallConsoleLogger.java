package de.fhws.ai.geneticalgorithm.logger;

import de.fhws.ai.geneticalgorithm.Individual;
import de.fhws.ai.geneticalgorithm.Population;

public class IntervallConsoleLogger implements Logger{

    ConsoleLogger logger = new ConsoleLogger();
    final int intervall;
    int counter = 0;
    public IntervallConsoleLogger(int intervall) {
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
