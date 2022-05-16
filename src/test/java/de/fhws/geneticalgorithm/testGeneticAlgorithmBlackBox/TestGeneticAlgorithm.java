package de.fhws.geneticalgorithm.testGeneticAlgorithmBlackBox;

import de.fhws.geneticalgorithm.*;
import de.fhws.geneticalgorithm.logger.ConsoleLogger;
import de.fhws.geneticalgorithm.selector.EliteSelector;
import de.fhws.geneticalgorithm.selector.RouletteWheelSelector;
import de.fhws.geneticalgorithm.selector.Selector;
import de.fhws.geneticalgorithm.selector.TournamentSelector;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class TestGeneticAlgorithm
{
	@Test
	public void testSeveralTimes() {
		for (int i = 0; i < 10000; i++)
		{
			testGeneticAlgorithmWithBlackBox();
		}

	}

	public void testGeneticAlgorithmWithBlackBox(){

		int[][] adjazenz = new int[][]
			{
				{0 ,10 ,3 ,5 ,9 ,8},
				{1 ,0 ,7 ,2 ,8 ,6},
				{5, 1, 0, 3, 7, 5},
				{6, 3, 2, 0, 1, 7},
				{6, 4, 3, 1, 0, 9},
				{5, 3, 7, 4, 1, 0}
			};

		Graph graph = new Graph(adjazenz);

		PopulationSupplier<TSP> popSup = () -> new Population<>(IntStream.range(0, 64).mapToObj( i -> TSP.genRandomSolution(graph)).collect(Collectors.toList()));
		Mutator<TSP> tspMutator = pop -> pop.getIndividuals().forEach(indi -> { if(Math.random() < 0.95 && pop.getBest() != indi ) indi.mutate(); });

		Recombiner<TSP> recombiner = (pop, size) -> {
			int i = 0;
			int initialSize = pop.getSize();
			while(pop.getSize() < size){
				pop.getIndividuals().add( pop.getIndividuals().get(i).copy() );
				i++;
				i = i % initialSize;
			}
		};

		GeneticAlgorithm<TSP> ga = new GeneticAlgorithm.Builder<TSP>(popSup, 20, new RouletteWheelSelector<>(0.2, true))
			.withRecombiner(recombiner)
			.withMutator(tspMutator)
			.withMutliThreaded(1)
			//.withLogger(new ConsoleLogger())
			.build();


		TSP result = ga.solve();
		assertEquals(14, result.getDist());
	}

}
