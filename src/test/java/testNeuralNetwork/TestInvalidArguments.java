package testNeuralNetwork;

import de.fhws.easyml.linearalgebra.Vector;
import de.fhws.easyml.ai.neuralnetwork.NeuralNet;
import org.junit.Test;

import static org.junit.Assert.fail;

public class TestInvalidArguments {

    @Test
    public void test_zero_inputSize( ) {
        testIllegalArgumentException( ( ) -> new NeuralNet.Builder( 0, 1 ) );
    }

    @Test
    public void test_zero_outputSize( ) {
        testIllegalArgumentException( ( ) -> new NeuralNet.Builder( 1, 0 ) );
    }

    @Test
    public void test_negative_inputSize( ) {
        testIllegalArgumentException( ( ) -> new NeuralNet.Builder( -1, 1 ) );
    }

    @Test
    public void test_negative_outputSize( ) {
        testIllegalArgumentException( ( ) -> new NeuralNet.Builder( 1, -1 ) );
    }

    @Test
    public void test_zero_layer( ) {
        testIllegalArgumentException( ( ) ->
                new NeuralNet.Builder( 1, 1 ).addLayer( 0 )
        );
    }

    @Test
    public void test_negative_layer( ) {
        testIllegalArgumentException( ( ) ->
                new NeuralNet.Builder( 1, 1 ).addLayer( -1 )
        );
    }

    @Test
    public void test_multiple_layers_zero( ) {
        testIllegalArgumentException( ( ) ->
                new NeuralNet.Builder( 1, 1 ).addLayers(0, 1 )
        );

        testIllegalArgumentException( ( ) ->
                new NeuralNet.Builder( 1, 1 ).addLayers(3, 0 )
        );

        testIllegalArgumentException( ( ) ->
                new NeuralNet.Builder( 1, 1 ).addLayers(3, 2, 0, 1 )
        );
    }

    @Test
    public void test_multiple_layers_negative( ) {
        testIllegalArgumentException( ( ) ->
                new NeuralNet.Builder( 1, 1 ).addLayers(-1, 1 )
        );

        testIllegalArgumentException( ( ) ->
                new NeuralNet.Builder( 1, 1 ).addLayers(3, -1 )
        );

        testIllegalArgumentException( ( ) ->
                new NeuralNet.Builder( 1, 1 ).addLayers(3, 2, -1, 1 )
        );
    }

    @Test
    public void test_incompatible_inputSize( ) {
        testIllegalArgumentException( ( ) -> new NeuralNet.Builder( 10, 1 ).build().calcOutput( new Vector( 9 ) ) );
    }

    private void testIllegalArgumentException( Runnable runnable ) {
        try {
            runnable.run( );
            fail( );
        } catch ( IllegalArgumentException e ) {

        }
    }

}
