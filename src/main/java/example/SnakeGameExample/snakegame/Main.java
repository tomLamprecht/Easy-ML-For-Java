package example.SnakeGameExample.snakegame;

import de.fhws.easyml.ai.geneticneuralnet.*;
import de.fhws.easyml.geneticalgorithm.GeneticAlgorithm;
import de.fhws.easyml.geneticalgorithm.evolution.Mutator;
import de.fhws.easyml.geneticalgorithm.evolution.Recombiner;
import de.fhws.easyml.geneticalgorithm.evolution.selectors.RouletteWheelSelector;
import de.fhws.easyml.geneticalgorithm.evolution.Selector;
import de.fhws.easyml.geneticalgorithm.logger.loggers.ConsoleLogger;
import de.fhws.easyml.geneticalgorithm.logger.loggers.graphplotter.lines.AvgFitnessLine;
import de.fhws.easyml.geneticalgorithm.logger.loggers.graphplotter.GraphPlotLogger;
import de.fhws.easyml.geneticalgorithm.logger.loggers.graphplotter.lines.MaxFitnessLine;
import de.fhws.easyml.geneticalgorithm.logger.loggers.graphplotter.lines.NQuantilFitnessLine;
import de.fhws.easyml.geneticalgorithm.logger.loggers.graphplotter.lines.WorstFitnessLine;
import de.fhws.easyml.geneticalgorithm.populationsupplier.PopulationSupplier;
import de.fhws.easyml.linearalgebra.Randomizer;
import de.fhws.easyml.ai.neuralnetwork.NeuralNet;

public class Main {

    public static final int MAX_GENS = 500;
    public static final Selector<NeuralNetIndividual> SELECTOR = new RouletteWheelSelector<>(0.4, true);
    public static final Recombiner<NeuralNetIndividual> RECOMBINER = new NNUniformCrossoverRecombiner(2);
    public static final Mutator<NeuralNetIndividual> MUTATOR = new NNRandomMutator(0.5, 0.6, new Randomizer(-0.1,0.1), 0.01);

    public static void main(String[] args) {
        NeuralNetSupplier nn =() -> new NeuralNet.Builder(25, 4)
                .build();

        NeuralNetFitnessFunction fitnessFunction = (neural) -> new SnakeAi(neural).startPlaying(100);

        PopulationSupplier<NeuralNetIndividual> populationSupplier = new NeuralNetPopulationSupplier(nn, fitnessFunction, 1000);

        GeneticAlgorithm<NeuralNetIndividual> ga = new GeneticAlgorithm.Builder<>(populationSupplier, MAX_GENS, SELECTOR)
                .withRecombiner(RECOMBINER)
                .withMutator(MUTATOR)
                .withLoggers(new ConsoleLogger(), new GraphPlotLogger(-1, "plot",
                        new AvgFitnessLine(),
                        new MaxFitnessLine(),
                        new WorstFitnessLine(),
                        new NQuantilFitnessLine(0.2),
                        new NQuantilFitnessLine(0.8)))
                .withMultiThreaded(16)
                .build();

        NeuralNet best = ga.solve().getNN();
        new SnakeAi(best).startPlayingWithDisplay();
    }


}
