package de.fhws.geneticalgorithm.testGeneticAlgorithmBlackBox;

import de.fhws.geneticalgorithm.*;
import de.fhws.geneticalgorithm.selector.*;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestGeneticAlgorithm
{
	private static final int TEST_RUNS = 100;
	private static final int EXPECTED_RESULT = 14;

	private static final double TSP_MUTATION_RATE = 0.95;
	private static final int POPULATION_SIZE = 64;
	private static final int MAX_GENS = 20;
	private static final double SELECTION_PERCENTAGE = 0.2;
	private static final int TOURNAMENT_SIZE = 12;

	private static final Graph graph = createTestGraph();
	private static final PopulationSupplier<TSP> popSup = createTspPopulationSupplier();
	private static final Mutator<TSP> tspMutator = createTspMutator();
	private static final Recombiner<TSP> tspRecombiner = createTspRecombiner();


	@Test
	public void testGAWithRouletteWheelSelectorMultipleTimes(){
		testSeveralTimes(this::testGeneticAlgorithmWithRouletteWheelSelector);
	}

	@Test
	public void testGAWithRouletteWheelSelectorMultiThreadedMultipleTimes(){
		testSeveralTimes(this::testGeneticAlgorithmWithRouletteWheelSelectorMultiThreaded);
	}

	@Test
	public void testGAWithEliteSelectorMultipleTimes(){
		testSeveralTimes(this::testGeneticAlgorithmWithEliteSelector);
	}

	@Test
	public void testGAWithEliteSelectorMultiThreadedMultipleTimes(){
		testSeveralTimes(this::testGeneticAlgorithmWithEliteSelectorMultiThreaded);
	}

	@Test
	public void testGAWithTournamentSelectorMultipleTimes(){
		testSeveralTimes(this::testGeneticAlgorithmWithTournamentSelector);
	}

	@Test
	public void testGAWithTournamentSelectorMultiThreadedMultipleTimes(){
		testSeveralTimes(this::testGeneticAlgorithmWithTournamentSelectorMultiThreaded);
	}

	@Test
	public void testGeneticAlgorithmShutdownException() {
		GeneticAlgorithm<TSP> ga = createTestTSPGeneticAlgorithm((pop, executor) -> {}, false);
		ga.solve();
		doSolveAfterShutdownOfGeneticAlgorithm(ga);
	}

	private void doSolveAfterShutdownOfGeneticAlgorithm(GeneticAlgorithm<TSP> ga)
	{
		try{
			ga.solve();
			fail();
		}catch(IllegalStateException e) {
			assertEquals(GeneticAlgorithm.ILLEGAL_OPERATION_AFTER_SHUTDOWN_MESSAGE, e.getMessage());
		}
	}

	public void testSeveralTimes(GeneticAlgorithmTester tester) {
		for (int i = 0; i < TEST_RUNS; i++)
			tester.test();
	}

	private void testGeneticAlgorithmWithRouletteWheelSelector(){
		createGeneticAlgorithmAndRun(new RouletteWheelSelector<>(SELECTION_PERCENTAGE, true), false);
	}

	private void testGeneticAlgorithmWithRouletteWheelSelectorMultiThreaded(){
		createGeneticAlgorithmAndRun(new RouletteWheelSelector<>(SELECTION_PERCENTAGE, true), true);
	}

	private void testGeneticAlgorithmWithEliteSelector(){
		createGeneticAlgorithmAndRun(new EliteSelector<>(SELECTION_PERCENTAGE), false);
	}

	private void testGeneticAlgorithmWithEliteSelectorMultiThreaded(){
		createGeneticAlgorithmAndRun(new EliteSelector<>(SELECTION_PERCENTAGE), true);
	}

	private void testGeneticAlgorithmWithTournamentSelector(){
		createGeneticAlgorithmAndRun(new TournamentSelector<>(SELECTION_PERCENTAGE, TOURNAMENT_SIZE), false);
	}

	private void testGeneticAlgorithmWithTournamentSelectorMultiThreaded(){
		createGeneticAlgorithmAndRun(new TournamentSelector<>(SELECTION_PERCENTAGE, TOURNAMENT_SIZE), true);
	}

	private void createGeneticAlgorithmAndRun(Selector<TSP> selector, boolean withMultiThreading)
	{
		GeneticAlgorithm<TSP> ga = createTestTSPGeneticAlgorithm(selector, withMultiThreading);

		TSP result = ga.solve();
		assertEquals(EXPECTED_RESULT, result.getDist());
	}

	private static GeneticAlgorithm<TSP> createTestTSPGeneticAlgorithm(Selector<TSP> selector, boolean withMultiThreading){
		return  new GeneticAlgorithm.Builder<TSP>(popSup, MAX_GENS, selector)
			.withRecombiner(tspRecombiner)
			.withMutator(tspMutator)
			.withMultiThreaded( withMultiThreading ? Runtime.getRuntime().availableProcessors() : 1 )
			.build();
	}

	@NotNull private static Graph createTestGraph() {
		final int[][] adjazenz = new int[][]
			{
				{0 ,10 ,3 ,5 ,9 ,8},
				{1 ,0 ,7 ,2 ,8 ,6},
				{5, 1, 0, 3, 7, 5},
				{6, 3, 2, 0, 1, 7},
				{6, 4, 3, 1, 0, 9},
				{5, 3, 7, 4, 1, 0}
			};

		return new Graph(adjazenz);
	}

	@NotNull private static PopulationSupplier<TSP> createTspPopulationSupplier()
	{
		return () -> new Population<>(IntStream.range(0, POPULATION_SIZE).mapToObj(i -> TSP.genRandomSolution(graph))
			.collect(Collectors.toList()));
	}

	private static Mutator<TSP> createTspMutator()
	{
		return (pop, executorService) -> pop.getIndividuals().forEach(indi -> {
			if (Math.random() < TSP_MUTATION_RATE && pop.getBest() != indi)
				indi.mutate();
		});
	}

	private static Recombiner<TSP> createTspRecombiner() {
		return (pop, size, executorService) -> {
			int i = 0;
			int initialSize = pop.getSize();
			while(pop.getSize() < size){
				pop.getIndividuals().add( pop.getIndividuals().get(i).copy() );
				i++;
				i = i % initialSize;
			}
		};
	}


}
