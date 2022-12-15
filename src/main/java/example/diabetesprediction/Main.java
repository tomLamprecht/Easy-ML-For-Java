package example.diabetesprediction;

import de.fhws.easyml.ai.geneticneuralnet.*;
import de.fhws.easyml.geneticalgorithm.GeneticAlgorithm;
import de.fhws.easyml.geneticalgorithm.evolution.Mutator;
import de.fhws.easyml.geneticalgorithm.evolution.Recombiner;
import de.fhws.easyml.geneticalgorithm.evolution.selectors.EliteSelector;
import de.fhws.easyml.geneticalgorithm.evolution.Selector;
import de.fhws.easyml.geneticalgorithm.logger.loggers.IntervalConsoleLogger;
import de.fhws.easyml.linearalgebra.Randomizer;
import de.fhws.easyml.ai.neuralnetwork.NeuralNet;

import java.io.IOException;


public class Main {
    private static final Selector<NeuralNetIndividual> SELECTOR = new EliteSelector<>( 0.3 );
    private static final Recombiner<NeuralNetIndividual> RECOMBINER = new NNUniformCrossoverRecombiner(2);
    private static final Mutator<NeuralNetIndividual> MUTATOR = new NNRandomMutator( 0.3, 0.10, new Randomizer( -0.5, 0.5 ), 0.01 );
    private static final int POP_SIZE = 10000;
    private static final int GENS = 2000;


    public static void main(String[] args) throws IOException {
        //Specificing a Neural Network with
        // input size 8,
        // output size 1,
        // 0 hiddenlayers
        // Random Weights between -0.1 and 0.1
        // Random Bias between -0.2 and 0.5
        NeuralNetSupplier neuralNetSupplier = ( ) -> new NeuralNet.Builder( 8, 1 )
                .withWeightRandomizer( new Randomizer( -0.1, 0.1 ) )
                .withBiasRandomizer( new Randomizer( -0.2, 0.5 ) )
                .build( );

        final InputParser inputParser = new InputParser();

        NeuralNetFitnessFunction fitnessFunction = (nn) -> {
            DiabetesDataSet data = inputParser.getRandomTrainingsDataSet();
            //Due to the default activation function in the Neural Network the output is between 0 and 1
            //If the output is greater than 0.5 we will interpret this as "Patient has diabetes"
            boolean prediction = nn.calcOutput(data.toVector()).get(0) > 0.5;
            return  prediction == data.hasDiabetes() ? 1 : 0; //If the prediction was correct return 1 otherwise return 0
        };

        NeuralNetPopulationSupplier nnPopSup= new NeuralNetPopulationSupplier(neuralNetSupplier, fitnessFunction, POP_SIZE);

        GeneticAlgorithm<NeuralNetIndividual> geneticAlgorithm = new GeneticAlgorithm.Builder<>(nnPopSup, GENS, SELECTOR)
                .withRecombiner(RECOMBINER)
                .withMutator(MUTATOR)
                .withMultiThreaded(16) //uses 16 Threads to process
                .withLoggers(new IntervalConsoleLogger(100)) //used to print logging info in the console
                .build();

        NeuralNetIndividual result = geneticAlgorithm.solve();
        testModel(inputParser, result.getNN());
    }

    public static void testModel(InputParser inputParser, NeuralNet model){
        long correctGuesses = inputParser.getUnseenData()
                .stream()
                .filter(data -> {
                    boolean prediction = model.calcOutput(data.toVector()).get(0) > 0.5;
                    System.out.println("prediction is " + prediction + " - patient has diabetes: " + data.hasDiabetes());
                    return prediction == data.hasDiabetes();
                })
                .count();
        System.out.println("The model guessed " + ((100d*correctGuesses)/InputParser.amountOfUnseenData) + "% correct");
    }
}
