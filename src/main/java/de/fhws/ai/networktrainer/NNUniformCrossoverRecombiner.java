package de.fhws.ai.networktrainer;

import de.fhws.ai.geneticalgorithm.Population;
import de.fhws.ai.geneticalgorithm.evolution.recombiner.Recombiner;
import de.fhws.ai.neuralnetwork.Layer;
import de.fhws.ai.utility.ListUtility;
import de.fhws.ai.utility.MultiThreadHelper;
import de.fhws.ai.utility.Validator;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;

public class NNUniformCrossoverRecombiner implements Recombiner<NeuralNetIndividual> {

    private int amountOfParentsPerChild;

    public NNUniformCrossoverRecombiner( int amountOfParentsPerChild ) {
        this.amountOfParentsPerChild = amountOfParentsPerChild;
    }

    @Override
    public void recombine( Population<NeuralNetIndividual> pop, int goalSize, ExecutorService executorService ) {
        Validator.value( amountOfParentsPerChild ).isBetweenOrThrow( 1, pop.getSize( ) );

        if ( executorService != null ) {
            recombineMultiThreaded( pop, goalSize, executorService );
        } else {
            recombineSingleThreaded( pop, goalSize );
        }
    }

    private void recombineSingleThreaded( Population<NeuralNetIndividual> pop, int goalSize ) {
        while ( pop.getIndividuals( ).size( ) < goalSize ) {
            List<NeuralNetIndividual> parents = ListUtility.selectRandomElements( pop.getIndividuals( ), amountOfParentsPerChild );
            pop.getIndividuals( ).add( makeChild( parents ) );
        }
    }

    private void recombineMultiThreaded( Population<NeuralNetIndividual> pop, int goalSize, ExecutorService executorService ) {

        int neededChildren = goalSize - pop.getIndividuals( ).size( );

        pop.getIndividuals( ).addAll(
                MultiThreadHelper.getListOutOfSupplier( executorService,
                        ( ) -> makeChild( ListUtility.selectRandomElements( pop.getIndividuals( ), amountOfParentsPerChild ) ),
                        neededChildren ) );
    }


    private NeuralNetIndividual makeChild( List<NeuralNetIndividual> parents ) {
        NeuralNetIndividual child = parents.get( 0 ).copy( );
        for ( int l = 0; l < child.getNN( ).getLayers( ).size( ); l++ ) {
            combineWeights( parents, child, l );

            combineBias( parents, child, l );
        }
        return child;
    }

    private void combineWeights( List<NeuralNetIndividual> parents, NeuralNetIndividual child, int layerIndex ) {
        Layer layer = child.getNN( ).getLayers( ).get( layerIndex );

        for ( int i = 0; i < layer.getWeights( ).getNumRows( ); i++ ) {
            for ( int j = 0; j < layer.getWeights( ).getNumCols( ); j++ ) {
                int selectedParent = ThreadLocalRandom.current( ).nextInt( parents.size( ) );
                layer.getWeights( )
                        .set( i, j, parents.get( selectedParent )
                                .getNN( ).getLayers( )
                                .get( layerIndex )
                                .getWeights( )
                                .get( i, j ) );
            }
        }
    }


    private void combineBias( List<NeuralNetIndividual> parents, NeuralNetIndividual child, int layerIndex ) {
        Layer layer = child.getNN( ).getLayers( ).get( layerIndex );

        for ( int i = 0; i < layer.getBias( ).size( ); i++ ) {
            int selectedParent = ThreadLocalRandom.current( ).nextInt( parents.size( ) );
            layer.getBias( )
                    .set( i, parents.get( selectedParent )
                            .getNN( ).getLayers( )
                            .get( layerIndex )
                            .getBias( )
                            .get( i ) );
        }
    }

    public static class EnsureSingleThreading extends NNUniformCrossoverRecombiner {

        public EnsureSingleThreading( int amountOfParentsPerChild ) {
            super( amountOfParentsPerChild );
        }

        @Override
        public void recombine( Population<NeuralNetIndividual> pop, int goalSize, ExecutorService executorService ) {
            super.recombine( pop, goalSize, null );
        }
    }

}
