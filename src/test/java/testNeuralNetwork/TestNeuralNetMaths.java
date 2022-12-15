package testNeuralNetwork;

import de.fhws.easyml.linearalgebra.Randomizer;
import de.fhws.easyml.linearalgebra.Vector;
import de.fhws.easyml.ai.neuralnetwork.NeuralNet;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestNeuralNetMaths {

    @Test
    public void test_mathematical_correctness_weights( ) {
        NeuralNet neuralNet = nnWithFixedWeightsAndBiases( 2, 0 );

        List<Vector> allLayers = neuralNet.calcAllLayer( new Vector( 1, 1, 1 ) );

        assertEquals( 2, allLayers.size( ) );

        assertEquals( new Vector( 6, 6 ), allLayers.get( 0 ) );
        assertEquals( new Vector( 24.0 ), allLayers.get( 1 ) );
    }

    @Test
    public void test_mathematical_correctness_biases( ) {
        NeuralNet neuralNet = nnWithFixedWeightsAndBiases( 1, 1 );

        List<Vector> allLayers = neuralNet.calcAllLayer( new Vector( 1, 1, 1 ) );

        assertEquals( 2, allLayers.size( ) );

        assertEquals( new Vector( 2, 2 ), allLayers.get( 0 ) );
        assertEquals( new Vector( 3.0 ), allLayers.get( 1 ) );
    }

    @Test
    public void test_mathematical_correctness_weights_and_biases( ) {
        NeuralNet neuralNet = nnWithFixedWeightsAndBiases( 2, 1 );

        List<Vector> allLayers = neuralNet.calcAllLayer( new Vector( 1, 1, 1 ) );

        assertEquals( 2, allLayers.size( ) );

        assertEquals( new Vector( 5, 5 ), allLayers.get( 0 ) );
        assertEquals( new Vector( 19.0 ), allLayers.get( 1 ) );
    }

    @Test
    public void test_mathematical_correctness_all_zero( ) {
        NeuralNet neuralNet = nnWithFixedWeightsAndBiases( 0, 0 );

        List<Vector> allLayers = neuralNet.calcAllLayer( new Vector( 1, 1, 1 ) );

        assertEquals( 2, allLayers.size( ) );

        assertEquals( new Vector( 0, 0 ), allLayers.get( 0 ) );
        assertEquals( new Vector( 0.0 ), allLayers.get( 1 ) );
    }

    @Test
    public void test_mathematical_correctness_negative_input( ) {
        NeuralNet neuralNet = nnWithFixedWeightsAndBiases( 2, 1 );

        List<Vector> allLayers = neuralNet.calcAllLayer( new Vector( -1, -1, -1 ) );

        assertEquals( 2, allLayers.size( ) );

        assertEquals( new Vector( -7, -7 ), allLayers.get( 0 ) );
        assertEquals( new Vector( -29.0 ), allLayers.get( 1 ) );
    }

    @Test
    public void test_mathematical_correctness_negative_weights( ) {
        NeuralNet neuralNet = nnWithFixedWeightsAndBiases( -2, 1 );

        List<Vector> allLayers = neuralNet.calcAllLayer( new Vector( 1, 1, 1 ) );

        assertEquals( 2, allLayers.size( ) );

        assertEquals( new Vector( -7, -7 ), allLayers.get( 0 ) );
        assertEquals( new Vector( 27.0 ), allLayers.get( 1 ) );
    }

    @Test
    public void test_mathematical_correctness_negative_biases( ) {
        NeuralNet neuralNet = nnWithFixedWeightsAndBiases( 2, -1 );

        List<Vector> allLayers = neuralNet.calcAllLayer( new Vector( 1, 1, 1 ) );

        assertEquals( 2, allLayers.size( ) );

        assertEquals( new Vector( 7, 7 ), allLayers.get( 0 ) );
        assertEquals( new Vector( 29.0 ), allLayers.get( 1 ) );
    }

    private static NeuralNet nnWithFixedWeightsAndBiases( double weights, double biases ) {
        return new NeuralNet.Builder( 3, 1 )
                .addLayer( 2 )
                .withActivationFunction( x -> x )
                .withWeightRandomizer( noRandomizeFixedValue( weights ) )
                .withBiasRandomizer( noRandomizeFixedValue( biases ) )
                .build( );
    }

    private static Randomizer noRandomizeFixedValue( double value ) {
        return new Randomizer( value, value );
    }


}
