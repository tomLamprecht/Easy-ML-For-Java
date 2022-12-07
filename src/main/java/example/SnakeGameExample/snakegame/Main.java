package example.SnakeGameExample.snakegame;

import de.fhws.ai.geneticalgorithm.GeneticAlgorithm;
import de.fhws.ai.geneticalgorithm.evolution.Mutator;
import de.fhws.ai.geneticalgorithm.evolution.recombiner.Recombiner;
import de.fhws.ai.geneticalgorithm.evolution.selector.RouletteWheelSelector;
import de.fhws.ai.geneticalgorithm.evolution.selector.Selector;
import de.fhws.ai.geneticalgorithm.logger.ConsoleLogger;
import de.fhws.ai.geneticalgorithm.logger.graphplotter.lines.AvgFitnessLine;
import de.fhws.ai.geneticalgorithm.logger.graphplotter.GraphPlotLogger;
import de.fhws.ai.geneticalgorithm.logger.graphplotter.lines.MaxFitnessLine;
import de.fhws.ai.geneticalgorithm.logger.graphplotter.lines.NQuantilFitnessLine;
import de.fhws.ai.geneticalgorithm.logger.graphplotter.lines.WorstFitnessLine;
import de.fhws.ai.geneticalgorithm.populationsupplier.PopulationSupplier;
import de.fhws.ai.linearalgebra.Randomizer;
import de.fhws.ai.networktrainer.*;
import de.fhws.ai.neuralnetwork.NeuralNet;

public class Main {

    public static final int MAX_GENS = 100;
    public static final Selector<NeuralNetIndividual> SELECTOR = new RouletteWheelSelector<>(0.4, true);
    public static final Recombiner<NeuralNetIndividual> RECOMBINER = new NNUniformCrossoverRecombiner(2);
    public static final Mutator<NeuralNetIndividual> MUTATOR = new NNRandomMutator(0.5, 0.6, new Randomizer(-0.1,0.1), 0.01);

    public static void main(String[] args) {
        NeuralNetSupplier nn =() -> new NeuralNet.Builder(25, 4)
                .build();

        NeuralNetFitnessFunction fitnessFunction = (neural) -> new SnakeAi(neural).startPlaying(1000);

        PopulationSupplier<NeuralNetIndividual> populationSupplier = new NeuralNetPopulationSupplier(nn, fitnessFunction, 1000);

        GeneticAlgorithm<NeuralNetIndividual> ga = new GeneticAlgorithm.Builder<NeuralNetIndividual>(populationSupplier,MAX_GENS, SELECTOR)
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
