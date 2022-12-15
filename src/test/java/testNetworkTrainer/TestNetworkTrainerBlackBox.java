package testNetworkTrainer;

import de.fhws.easyml.ai.geneticneuralnet.*;
import de.fhws.easyml.geneticalgorithm.GeneticAlgorithm;
import de.fhws.easyml.geneticalgorithm.evolution.Mutator;
import de.fhws.easyml.geneticalgorithm.evolution.Recombiner;
import de.fhws.easyml.geneticalgorithm.evolution.selectors.EliteSelector;
import de.fhws.easyml.geneticalgorithm.evolution.Selector;
import de.fhws.easyml.linearalgebra.Randomizer;
import de.fhws.easyml.linearalgebra.Vector;
import de.fhws.easyml.ai.neuralnetwork.NeuralNet;
import de.fhws.easyml.utility.Validator;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestNetworkTrainerBlackBox {

    private static final Selector<NeuralNetIndividual> SELECTOR = new EliteSelector<>( 0.1 );
    private static final Recombiner<NeuralNetIndividual> RECOMBINER = new NNUniformCrossoverRecombiner( 2 );
    private static final Mutator<NeuralNetIndividual> MUTATOR = new NNRandomMutator( 0.9, 0.4, new Randomizer( -0.01, 0.01 ), 0.01 );

    private static final int NUMBER = 5;
    private static final int GENS = 50;
    private static final int POP_SIZE = 200;
    private static final int ASSERT_TIMES = 10;
    private static final double TOLERANCE = 0.2;
    private static final int DO_TEST_TIMES = 10;
    private static final double PERCENTAGE_TO_PASS = 0.8;

    @Test
    public void test_simple_number_prediction( ) {

        int countSuccess = 0;

        for ( int i = 0; i < DO_TEST_TIMES; i++ ) {
            NeuralNet best = evolveNetwork( NUMBER, GENS, POP_SIZE );

            double diff = 0;
            for ( int j = 0; j < ASSERT_TIMES; j++ ) {
                diff += calcDiffSimpleNumber( best, j / 10.0, NUMBER );
            }

            if ( Validator.value( diff ).isBetween( 0, TOLERANCE * ASSERT_TIMES ) )
                countSuccess++;
        }

        System.out.println( "Neural net success: " + countSuccess + " / " + DO_TEST_TIMES );
        assertTrue( countSuccess >= DO_TEST_TIMES * PERCENTAGE_TO_PASS );
    }

    private NeuralNet evolveNetwork( int NUMBER, int GENS, int POP_SIZE ) {
        NeuralNetSupplier neuralNetSupplier = ( ) -> new NeuralNet.Builder( 1, 1 )
                .addLayer( 2 )
                .withActivationFunction( x -> x )
                .withWeightRandomizer( new Randomizer( -10, 10 ) )
                .withBiasRandomizer( new Randomizer( 0, 2 ) )
                .build( );

        NeuralNetFitnessFunction fitnessFunction = neuralNet -> {
            double diff = 0;

            for ( int i = 0; i < 10; i++ ) {
                diff += calcDiffSimpleNumber( neuralNet, Math.random( ), NUMBER );
            }

            return diff == 0 ? Double.MAX_VALUE : 1 / diff;
        };

        NeuralNetPopulationSupplier supplier = new NeuralNetPopulationSupplier( neuralNetSupplier, fitnessFunction, POP_SIZE );

        GeneticAlgorithm<NeuralNetIndividual> geneticAlgorithm =
                new GeneticAlgorithm.Builder<>( supplier, GENS, SELECTOR )
                        .withRecombiner( RECOMBINER )
                        .withMutator( MUTATOR )
                        .build( );

        return geneticAlgorithm.solve( ).getNN( );
    }

    private static double calcDiffSimpleNumber( NeuralNet neuralNet, double x, double y ) {
        Vector input = new Vector( x );
        Vector output = neuralNet.calcOutput( input );
        return Math.abs( output.get( 0 ) - y );
    }


}
