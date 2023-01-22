package testNeuralNetwork;

import de.fhws.easyml.ai.backpropagation.BackpropagationTrainer;
import de.fhws.easyml.ai.neuralnetwork.NeuralNet;
import de.fhws.easyml.ai.neuralnetwork.activationfunction.Tanh;
import de.fhws.easyml.ai.neuralnetwork.costfunction.SummedCostFunction;
import de.fhws.easyml.linearalgebra.Randomizer;
import de.fhws.easyml.linearalgebra.Vector;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TestBackpropagation {

    @Test
    public void testBackpropagation(){
        NeuralNet neuralNet = new NeuralNet.Builder( 2, 1 ).addLayer( 5 )
                .withBiasRandomizer( new Randomizer( 0,2 ) )
                .withWeightRandomizer( new Randomizer( -1,1 ) )
                .withActivationFunction(new Tanh())
                .build();

        new BackpropagationTrainer.Builder(neuralNet, () -> new BackpropagationTrainer.Batch(getInput(), getOutput()), 0.5, 1000)
               // .withLogger(new ConsoleLogger())
                .build()
                .train();

        for (int i = 0; i < 4; i++) {
            double costs = new SummedCostFunction().costs(getOutput().get(i), neuralNet.calcOutput(getInput().get(i)));
            assertEquals(0, costs, 0.01);
        }
    }


    @NotNull
    private List<Vector> getOutput() {
        Vector expTrue = new Vector( 1 );
        expTrue.set( 0, 1 );
        Vector expFalse = new Vector( 1 );
        return Stream.of( expFalse, expTrue, expTrue, expFalse ).collect( Collectors.toList());
    }

    @NotNull
    private List<Vector> getInput() {
        Vector input1 = new Vector( 0,0 );
        Vector input2 = new Vector(0,0.76);
        Vector input3 = new Vector( 0.76,0 );
        Vector input4 = new Vector( 0.76,0.76 );
        return Stream.of(input1, input2, input3, input4).collect( Collectors.toList());
    }

}
