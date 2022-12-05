package example;

import de.fhws.ai.geneticalgorithm.GeneticAlgorithm;
import de.fhws.ai.geneticalgorithm.evolution.Mutator;
import de.fhws.ai.geneticalgorithm.evolution.recombiner.FillUpRecombiner;
import de.fhws.ai.geneticalgorithm.evolution.recombiner.Recombiner;
import de.fhws.ai.geneticalgorithm.evolution.selector.EliteSelector;
import de.fhws.ai.geneticalgorithm.evolution.selector.Selector;
import de.fhws.ai.geneticalgorithm.logger.IntervallConsoleLogger;
import de.fhws.ai.linearalgebra.Randomizer;
import de.fhws.ai.linearalgebra.Vector;
import de.fhws.ai.networktrainer.*;
import de.fhws.ai.neuralnetwork.NeuralNet;

import java.util.function.DoubleUnaryOperator;

public class SimpleFunctionPredictionExample {

    private static final Selector<NeuralNetIndividual> SELECTOR = new EliteSelector<>( 0.1 );
    private static final Recombiner<NeuralNetIndividual> RECOMBINER = new FillUpRecombiner<>( );
    private static final Mutator<NeuralNetIndividual> MUTATOR = new NNRandomMutator( 0.9, 0.5, new Randomizer( -0.01, 0.01 ), 0.01 );

    public static final int POP_SIZE = 1000;

    public static final int GENS = 5000;

    public void predict(){
        //Specificing a Neural Network with
        // input size 1,
        // output size 1,
        // 2 hiddenlayers (size 10 and 5)
        // Random Weights between -10 and 10
        // Random Bias between 0 and 2
        NeuralNetSupplier neuralNetSupplier = ( ) -> new NeuralNet.Builder( 1, 1 )
                .addLayer( 3 )
                .withActivationFunction( x -> x )
                .withWeightRandomizer( new Randomizer( -10, 10 ) )
                .withBiasRandomizer( new Randomizer( 0, 2 ) )
                .build( );

        //This fitness function will return higher results if the output of the neuralnet is
        //closer to the result of f(x) = 2x
        NeuralNetFitnessFunction fitnessFunction = (nn) -> {
            double input = Math.random() * 100;
            double output = nn.calcOutput(new Vector( input )).get(0);
            double expectedValue = 2 * input;
            double diff = Math.abs(output - expectedValue);
            return diff == 0 ? Double.MAX_VALUE : 1 / diff;
        };

        NeuralNetPopulationSupplier nnPopSup= new NeuralNetPopulationSupplier(neuralNetSupplier, fitnessFunction, POP_SIZE);

       GeneticAlgorithm<NeuralNetIndividual> geneticAlgorithm = new GeneticAlgorithm.Builder<>(nnPopSup, GENS, SELECTOR)
               .withRecombiner(RECOMBINER)
               .withMutator(MUTATOR)
               .withMultiThreaded(16) //uses 16 Threads to process
               .withLoggers(new IntervallConsoleLogger(100)) //used to print logging info in the console
               .build();

       NeuralNetIndividual result = geneticAlgorithm.solve();

        testResult(e -> result.calcOutput(new Vector(e)).get(0));
    }

    public void testResult(DoubleUnaryOperator function) {

        for (int i = 0; i < 100; i++) {
            double input = Math.random() * 100;
            System.out.println("Expecting: " + (input*2) + " Prediction: " + function.applyAsDouble(input));
        }

    }

    public static void main(String[] args) {
        new SimpleFunctionPredictionExample().predict();
    }

}
