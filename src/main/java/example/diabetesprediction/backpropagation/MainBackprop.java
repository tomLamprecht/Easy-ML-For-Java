package example.diabetesprediction.backpropagation;

import de.fhws.easyml.ai.backpropagation.BackpropagationTrainer;
import de.fhws.easyml.ai.backpropagation.logger.loggers.ConsoleLogger;
import de.fhws.easyml.ai.geneticneuralnet.*;
import de.fhws.easyml.ai.neuralnetwork.NeuralNet;
import de.fhws.easyml.ai.neuralnetwork.activationfunction.Sigmoid;
import de.fhws.easyml.ai.neuralnetwork.activationfunction.Tanh;
import de.fhws.easyml.geneticalgorithm.GeneticAlgorithm;
import de.fhws.easyml.geneticalgorithm.evolution.Mutator;
import de.fhws.easyml.geneticalgorithm.evolution.Recombiner;
import de.fhws.easyml.geneticalgorithm.evolution.Selector;
import de.fhws.easyml.geneticalgorithm.evolution.selectors.EliteSelector;
import de.fhws.easyml.geneticalgorithm.logger.loggers.IntervalConsoleLogger;
import de.fhws.easyml.linearalgebra.Randomizer;
import de.fhws.easyml.linearalgebra.Vector;
import example.diabetesprediction.DiabetesDataSet;
import example.diabetesprediction.InputParser;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainBackprop {

    public static void main(String[] args) throws IOException {
        //Specificing a Neural Network with
        // input size 8,
        // output size 1,
        // 0 hiddenlayers
        // Random Weights between -0.1 and 0.1
        // Random Bias between -0.2 and 0.
        NeuralNet neuralNet = new NeuralNet.Builder(8, 1)
                .addLayer(5)
                .addLayer(2)
                .withWeightRandomizer(new Randomizer(-0.005, 0.005))
                .withBiasRandomizer(new Randomizer(0, 0.01))
                .withActivationFunction(new Sigmoid())
                .build();

        final InputParser inputParser = new InputParser();

        Supplier<BackpropagationTrainer.Batch> batchSupplier = () -> {
            List<DiabetesDataSet> dataList = Stream.generate(inputParser::getRandomTrainingsDataSet).limit(10).collect(Collectors.toList());
            List<Vector> expectedOutput = dataList.stream().map(set -> {
                Vector vec = new Vector(1);
                vec.set(0, set.hasDiabetes() ? 1 : 0);
                return vec;
            }).collect(Collectors.toList());
            return new BackpropagationTrainer.Batch(dataList.stream().map(DiabetesDataSet::toVector).collect(Collectors.toList()), expectedOutput );
        };

        new BackpropagationTrainer.Builder(neuralNet, batchSupplier, 0.05, 100000)
                .withLogger(new ConsoleLogger())
                .build()
                .train();

       // testModel(inputParser, neuralNet);
    }

    public static void testModel(InputParser inputParser, NeuralNet model) {
        long correctGuesses = inputParser.getUnseenData()
                .stream()
                .filter(data -> {
                    boolean prediction = model.calcOutput(data.toVector()).get(0) > 0.5;
                    System.out.println("prediction is " + prediction + " - patient has diabetes: " + data.hasDiabetes());
                    return prediction == data.hasDiabetes();
                })
                .count();
        System.out.println("The model guessed " + ((100d * correctGuesses) / InputParser.amountOfUnseenData) + "% correct");
    }
}
