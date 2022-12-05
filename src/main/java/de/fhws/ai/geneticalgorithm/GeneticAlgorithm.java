package de.fhws.ai.geneticalgorithm;

import de.fhws.ai.geneticalgorithm.evolution.Mutator;
import de.fhws.ai.geneticalgorithm.logger.Logger;
import de.fhws.ai.geneticalgorithm.evolution.recombiner.Recombiner;
import de.fhws.ai.geneticalgorithm.evolution.selector.Selector;
import de.fhws.ai.geneticalgorithm.populationsupplier.PopulationSupplier;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntConsumer;

public class GeneticAlgorithm<T extends Individual<T>> {

    public static final String ILLEGAL_OPERATION_AFTER_SHUTDOWN_MESSAGE = "Genetic Algorithm already shutdowned";

    private int size;
    private final int maxGens;
    private final Population<T> population;
    private final Selector<T> selector;
    private final Optional<Recombiner<T>> recombiner;
    private final Optional<Mutator<T>> mutator;

    private final Optional<IntConsumer> genPreperator;
    private final Optional<IntervalSaver> saver;
    private final List<Logger> loggers;

    private Optional<ExecutorService> executor = Optional.empty();

    private final AtomicBoolean shutdowned = new AtomicBoolean(false);

    private GeneticAlgorithm( PopulationSupplier<T> popSupplier, int maxGens, Selector<T> selector,
                              Recombiner<T> recombiner, Mutator<T> mutator, IntConsumer genPreperator, IntervalSaver saver, List<Logger> loggers,
                              int amountThreads) {
        this.population = popSupplier.get();
        this.maxGens = maxGens + population.getGeneration();
        this.size = population.getSize();
        this.selector = selector;
        this.recombiner = Optional.ofNullable(recombiner);
        this.mutator = Optional.ofNullable(mutator);
        this.genPreperator = Optional.ofNullable(genPreperator);
        this.saver = Optional.ofNullable(saver);
        this.loggers = loggers;

        if (amountThreads > 1)
            executor = Optional.of(Executors.newWorkStealingPool( amountThreads ));

    }

    /**
     * Solves the given problem with an evolutional approach.
     *
     * @return T the developed solution for the problem.
     * @throws IllegalStateException if GeneticAlgorithm is already shutdowned
     */
    public T solve() {
        validateShutdownState();

        evolute();

        return getBestAndShutdown();
    }

    private T getBestAndShutdown()
    {
        T best = getBestIndividual();

        shutdownGeneticAlgorithm();

        return best;
    }

    private void evolute()
    {
        for (int i = population.getGeneration(); i < maxGens; i++)
            nextGen();
    }

    private void validateShutdownState()
    {
        if(isShutdowned())
            throw new IllegalStateException(ILLEGAL_OPERATION_AFTER_SHUTDOWN_MESSAGE);
    }

    private void shutdownGeneticAlgorithm()
    {
        shutdowned.set(true);
        executor.ifPresent( ExecutorService::shutdown );
    }

    private T getBestIndividual() {
        calculateFitnesses();
        return population.getBest();
    }

    private void nextGen() {
        prepareNextEvolution();

        evoluteNextGen();

        doEvolutionFollowUp();
    }

    private void prepareNextEvolution() {
        callGenPreperator();

        calculateFitnesses();


        population.incGeneration();
    }

    private void doEvolutionFollowUp() {
        callSaver();

        callLoggers();
    }

    private void callLoggers() {
        loggers.forEach(logger -> logger.log(maxGens, population));
    }

    private void callSaver() {
        saver.ifPresent(saver -> saver.save(population));
    }

    private void evoluteNextGen() {
        doSelection();

        doRecombination();

        doMutation();
    }

    private void calculateFitnesses() {
        population.calcFitnesses(executor);
    }

    private void doMutation() {
        mutator.ifPresent(m -> m.mutate(population, executor));
    }

    private void doRecombination() {
        recombiner.ifPresent(r -> r.recombine(population, size, executor));
        size = population.getSize();
    }

    private void doSelection() {
        selector.select(population, executor);
    }

    private void callGenPreperator() {
        genPreperator.ifPresent(c -> c.accept(population.getGeneration()));
    }

    public boolean isShutdowned()
    {
        return shutdowned.get();
    }

    public static class Builder<T extends Individual<T>> {

        private final int maxGens;
        private final PopulationSupplier<T> popSupplier;
        private final Selector<T> selector;
        private Recombiner<T> recombiner;
        private Mutator<T> mutator;

        private IntConsumer genPreperator;
        private IntervalSaver saver;
        private List<Logger> loggers = new ArrayList<>();

        private int amountThreads;

        /**
         * Creates a Builder for a Generic Algorithm.
         * @param popSupplier provides a initial Population
         * @param maxGens are the amount of generation that should be computed
         * @param selector is used for the selection process
         * @see <a href="https://docs.oracle.com/middleware/1221/jdev/api-reference-esdk/oracle/javatools/patterns/Builder.html">Java-Doc for Builder-Pattern</a>
         */
        public Builder(PopulationSupplier<T> popSupplier, int maxGens, Selector<T> selector) {
            this.selector = selector;
            this.maxGens = maxGens;
            this.popSupplier = popSupplier;
        }

        /**
         * The underlying Genetic Algorithm will use the given {@code recombiner} to refill the population in the recombination-process.
         * @param recombiner provides the recombination method
         * @return itself, due to Builder-Pattern
         */
        public Builder<T> withRecombiner(Recombiner<T> recombiner) {
            this.recombiner = recombiner;
            return this;
        }

        /**
         * The underlying Genetic Alogrithm will use the give {@code mutator} to mutate the population <strong>after</strong> the recombination-process
         * @param mutator provides the mutation method
         * @return itself, due to Builder-Pattern
         */
        public Builder<T> withMutator(Mutator<T> mutator) {
            this.mutator = mutator;
            return this;
        }

        /**
         * The underlying Genetic Algorithm will call the genPreperator before every new Generation. It can be used e.g. to synchronize Input-Data for the fitness calculation.
         * @param genPreperator provides method that gets called
         * @return itself, due to Builder-Pattern
         */
        public Builder<T> withGenPreperator(IntConsumer genPreperator) {
            this.genPreperator = genPreperator;
            return this;
        }

        /**
         * used to save populations to file
         *
         * @param dir      is a directory where the population will be saved in
         * @param interval is the interval of which the populations should be saved
         * @param override determines whether a new file should be created each time or the old one overridden
         * @return itself, due to Builder-Pattern
         **/
        public Builder<T> withSaveToFile(File dir, int interval, boolean override) {
            this.saver = new IntervalSaver(interval, override, dir);
            return this;
        }

        /**
         * The Loggers will be called after every generation and can be used to log the current state of the solving process
         * @param loggers provides logging method
         * @return itself, due to Builder-Pattern
         */
        public Builder<T> withLoggers(Logger ... loggers) {
            this.loggers.addAll(Arrays.asList(loggers));
            return this;
        }

        /**
         * Is used to controll the Multi-Thread behavior of the Genetic Algorith. If {@code amountThreads} is greater than 1 the underlying Genetic Algorithm will use a MultiThread approach if the given Implementations of
         * {@link Selector}, {@link Recombiner} and {@link Mutator} support Multi-Threading.
         * @param amountThreads are the maximal number of Threads available in the Threadpool
         * @return itself, due to Builder-Patten
         * @throws IllegalArgumentException if {@code amountThreads} is smaller than 1
         */
        public Builder<T> withMultiThreaded(int amountThreads) {
            if (amountThreads < 1)
                throw new IllegalArgumentException("amount of threads must be in at least 1");
            this.amountThreads = amountThreads;
            return this;
        }

        /**
         * builds the Genetic Algorithm with the previously given Attributes
         * @return build Genetic Algorithm
         */
        public GeneticAlgorithm<T> build() {
            return new GeneticAlgorithm<>(popSupplier, maxGens, selector, recombiner, mutator, genPreperator, saver,
                    loggers, amountThreads);
        }
    }

}
