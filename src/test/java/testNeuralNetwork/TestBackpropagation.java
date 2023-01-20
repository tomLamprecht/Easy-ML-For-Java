package testNeuralNetwork;

import de.fhws.easyml.ai.neuralnetwork.Layer;
import de.fhws.easyml.ai.neuralnetwork.NeuralNet;
import de.fhws.easyml.ai.neuralnetwork.activationfunction.Tanh;
import de.fhws.easyml.ai.neuralnetwork.costfunction.SummedCostFunction;
import de.fhws.easyml.linearalgebra.Matrix;
import de.fhws.easyml.linearalgebra.Randomizer;
import de.fhws.easyml.linearalgebra.Vector;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestBackpropagation {

    @Test
    public void testBackpropagation(){

        NeuralNet neuralNet = new NeuralNet.Builder( 2, 1 ).addLayer( 3 )
                .withBiasRandomizer( new Randomizer( 0,2 ) )
                .withWeightRandomizer( new Randomizer( -1,1 ) )
                .withActivationFunction(new Tanh())
                .build();


        System.out.println("BIAS:");
        neuralNet.getLayers().stream().map(Layer::getBias).forEach(System.out::println);
        System.out.println("BIAS END ");

        neuralNet.getLayers().stream().map( Layer::getWeights ).map( Matrix::getData ).map( Arrays::deepToString ).forEach( System.out::println );
        List<Vector> input = getInput();
        List<Vector> output = getOutput();

        for ( int i = 0; i < 10000; i++ ) {
            neuralNet.trainBatch( input , output , new SummedCostFunction(), 0.2 );
        }
        System.out.println();
        neuralNet.getLayers().stream().map( Layer::getWeights ).map( Matrix::getData ).map( Arrays::deepToString ).forEach( System.out::println );

        System.out.println("BIAS:");
        neuralNet.getLayers().stream().map(Layer::getBias).forEach(System.out::println);
        System.out.println("BIAS END ");

        for ( int i = 0; i < input.size(); i++ ) {
            Vector result = neuralNet.calcOutput( input.get( i ) );
            System.out.println("The result was " + result + " but should be " + output.get( i ));

        }

    }

    @NotNull
    private List<Vector> getOutput() {
        Vector expTrue = new Vector( 1 );
        expTrue.set( 0, 1 );
        Vector expFalse = new Vector( 1 );
        List<Vector> output = Stream.of( expFalse, expTrue, expTrue, expFalse ).collect( Collectors.toList());
        return output;
    }

    @NotNull
    private List<Vector> getInput() {
        Vector input1 = new Vector( 0,0 );
        Vector input2 = new Vector(0,0.76);
        Vector input3 = new Vector( 0.76,0 );
        Vector input4 = new Vector( 0.76,0.76 );
        List<Vector> input = Stream.of(input1, input2, input3, input4).collect( Collectors.toList());
        return input;
    }

}
