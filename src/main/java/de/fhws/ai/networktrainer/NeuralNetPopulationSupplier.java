package de.fhws.ai.networktrainer;

import de.fhws.ai.geneticalgorithm.Population;
import de.fhws.ai.geneticalgorithm.populationsupplier.PopulationSupplier;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NeuralNetPopulationSupplier implements PopulationSupplier<NeuralNetIndividual> {

    private final NeuralNetSupplier neuralNetSupplier;
    private final int populationSize;
    private final NeuralNetFitnessFunction fitnessFunction;

    public NeuralNetPopulationSupplier( NeuralNetSupplier neuralNetSupplier, NeuralNetFitnessFunction fitnessFunction, int populationSize ) {
        this.neuralNetSupplier = neuralNetSupplier;
        this.populationSize = populationSize;
        this.fitnessFunction = fitnessFunction;
    }

    @Override
    public Population<NeuralNetIndividual> get( ) {
        return new Population<>( IntStream.range( 0, populationSize )
                .mapToObj( i -> new NeuralNetIndividual( neuralNetSupplier.get(), fitnessFunction ) )
                .collect( Collectors.toList( ) ) );
    }
}
