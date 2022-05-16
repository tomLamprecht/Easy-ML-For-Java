package de.fhws.atest;


import de.fhws.atest.snakegame.SnakeAi;
import de.fhws.geneticalgorithm.GeneticAlgorithm;
import de.fhws.geneticalgorithm.Population;
import de.fhws.geneticalgorithm.PopulationSupplier;
import de.fhws.geneticalgorithm.logger.ConsoleLogger;
import de.fhws.geneticalgorithm.selector.EliteSelector;
import de.fhws.networktrainer.NetworkTrainer;
import de.fhws.networktrainer.NeuralNetFitnessFunction;
import de.fhws.networktrainer.NeuralNetIndividual;

public class Main {

    public static void main(String[] args) {
        NeuralNetFitnessFunction fitFunc = nn -> new SnakeAi(nn).startPlaying(50);
        PopulationSupplier<NeuralNetIndividual> supplier = () -> {
            Population<NeuralNetIndividual> pop = new Population<>();
            for (int i = 0; i < 1000; i++) {
                pop.getIndividuals().add(new NeuralNetIndividual(new de.fhws.networks.neuralnetworks.NeuralNet.Builder(25, 4)
                .addLayer(16)
                .build(), fitFunc));

            }
            return pop;
        };
        NeuralNetIndividual best = new NetworkTrainer<NeuralNetIndividual>(
                new GeneticAlgorithm.Builder<NeuralNetIndividual>(supplier, 100, new EliteSelector<>(0.05))
                .withLogger(new ConsoleLogger())
                .withRecombiner(new SnakeRecombiner())
                .withMutator(new SnakeMutator())
                .build())

                .train();

        new SnakeAi(best.getNN()).startPlayingWithDisplay();
    }

}
