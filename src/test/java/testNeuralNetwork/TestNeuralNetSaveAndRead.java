package testNeuralNetwork;

import de.fhws.ai.linearalgebra.Vector;
import de.fhws.ai.neuralnetwork.NeuralNet;
import de.fhws.ai.utility.FileHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestNeuralNetSaveAndRead {

    private File file;
    private NeuralNet testNN;
    private static final String pathToTestFile = "testFiles/testNN.ser";

    @Before
    public void prepare( ) throws IOException {
        this.file = new File( pathToTestFile );
        this.testNN = new NeuralNet.Builder( 2, 1 )
                .addLayer( 2 )
                .withActivationFunction( x -> x )
                .build();

        if ( !file.createNewFile( ) )
            fail( );
    }

    @Test
    public void test_save_and_read( ) {

        FileHandler.writeObjectToFile( testNN, pathToTestFile, true );

        NeuralNet neuralNet = ( NeuralNet ) FileHandler.getFirstObjectFromFile( pathToTestFile );

        Vector input = new Vector( 10, 20 );
        assertEquals( testNN.calcOutput( input ), neuralNet.calcOutput( input ) );
    }

    @After
    public void cleanUp( ) {
        if ( !file.delete( ) )
            fail( );
    }

}
