package de.fhws.ai.geneticalgorithm.evolution.recombiner;

import de.fhws.ai.geneticalgorithm.Individual;
import de.fhws.ai.geneticalgorithm.Population;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

public class FillUpRecombiner<T extends Individual<T>> implements Recombiner<T> {

    @Override
    public void recombine( Population<T> pop, int goalSize, Optional<ExecutorService> executorService ) {
        while ( pop.getSize() < goalSize ) {
            pop.getIndividuals().add( pop.getIndividuals().get( (int) ( Math.random() * pop.getSize() ) ).copy() );
        }
    }
}
