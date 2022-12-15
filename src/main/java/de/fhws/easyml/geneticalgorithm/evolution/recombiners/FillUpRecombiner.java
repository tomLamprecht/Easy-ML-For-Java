package de.fhws.easyml.geneticalgorithm.evolution.recombiners;

import de.fhws.easyml.geneticalgorithm.Individual;
import de.fhws.easyml.geneticalgorithm.Population;
import de.fhws.easyml.geneticalgorithm.evolution.Recombiner;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutorService;

public class FillUpRecombiner<T extends Individual<T>> implements Recombiner<T> {

    @Override
    public void recombine(Population<T> pop, int goalSize, @Nullable ExecutorService executorService ) {
        while ( pop.getSize() < goalSize ) {
            pop.getIndividuals().add( pop.getIndividuals().get( (int) ( Math.random() * pop.getSize() ) ).copy() );
        }
    }
}
