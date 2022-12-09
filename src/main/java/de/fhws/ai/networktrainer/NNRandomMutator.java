package de.fhws.ai.networktrainer;

import de.fhws.ai.geneticalgorithm.Population;
import de.fhws.ai.geneticalgorithm.evolution.Mutator;
import de.fhws.ai.linearalgebra.Randomizer;
import de.fhws.ai.neuralnetwork.NeuralNet;
import de.fhws.ai.utility.MultiThreadHelper;
import de.fhws.ai.utility.Validator;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Stream;

public class NNRandomMutator implements Mutator<NeuralNetIndividual> {

    private final double outerMutationRate;

    private final DoubleUnaryOperator innerMutator;

    /**
     * Creates a Random Mutator for Neural Networks
     * It will choose a Neural Net out of the population after the probability {@code outMutationRate}
     * Every Weight of chosen Neural Net will be mutated by the probability of {@code innerMutationRate}
     * for the mutation there will be a factor of the modifying weight calculated by a random number of {@code mutationFactor} like:
     * {@code mutationFactor.getInRange() * weight}. Then there will be checked if its absolute value is smaller than
     * {@code minMutationAbsolute}, if yes the minMutationAbsolute with the sign of the factor previously calculated will
     * be added to weight. Otherwise, the factor itself gets added to the weight.
     *
     * @param outerMutationRate   of the population
     * @param innerMutationRate   of the chosen Neural Net
     * @param mutationFactor      modifying Factor
     * @param minMutationAbsolute minimum modifying value
     */
    public NNRandomMutator( double outerMutationRate, double innerMutationRate, Randomizer mutationFactor, double minMutationAbsolute ) {
        Validator.value( outerMutationRate ).isBetweenOrThrow( 0, 1 );
        Validator.value( innerMutationRate ).isBetweenOrThrow( 0, 1 );

        this.outerMutationRate = outerMutationRate;

        innerMutator = d -> {
            if ( ThreadLocalRandom.current( ).nextDouble( ) < innerMutationRate ) {
                double mutateValue = d * mutationFactor.getInRange( );
                return d + ( mutateValue < 0 ? -1 : 1 ) * Math.max( minMutationAbsolute, Math.abs( mutateValue ) );
            }
            return d;
        };
    }

    @Override
    public void mutate( Population<NeuralNetIndividual> pop, @Nullable ExecutorService executorService ) {
        Stream<NeuralNetIndividual> filteredIndividuals = getFilteredIndividuals( pop );

        Consumer<NeuralNetIndividual> mutateConsumer = individual -> mutateNN( individual.getNN( ) );
        if ( executorService != null )
            MultiThreadHelper.callConsumerOnStream( executorService, filteredIndividuals, mutateConsumer );
        else
            filteredIndividuals
                    .forEach( mutateConsumer );
    }

    private Stream<NeuralNetIndividual> getFilteredIndividuals( Population<NeuralNetIndividual> pop ) {
        return pop.getIndividuals( )
                .stream( )
                .filter( individual -> ThreadLocalRandom.current( ).nextDouble( ) < outerMutationRate );
    }

    private void mutateNN( NeuralNet neuralNet ) {
        neuralNet.getLayers( )
                .forEach( layer -> {
                    layer.getWeights( ).apply( innerMutator );
                    layer.getBias( ).apply( innerMutator );
                } );
    }

    public static class EnsureSingleThreading extends NNRandomMutator {

        /**
         * Creates a Random Mutator for Neural Networks but ensures to take the Single Thread implementation even if
         * a ExecuterService is provided
         * for further documentation see {@link #NNRandomMutator(double, double, Randomizer, double)}
         */

        public EnsureSingleThreading( double outerMutationRate, double innerMutationRate, Randomizer mutationFactor, double minMutationAbsolute ) {
            super( outerMutationRate, innerMutationRate, mutationFactor, minMutationAbsolute );
        }

        @Override
        public void mutate( Population<NeuralNetIndividual> pop, ExecutorService executorService ) {
            super.mutate( pop, null );
        }
    }
}
