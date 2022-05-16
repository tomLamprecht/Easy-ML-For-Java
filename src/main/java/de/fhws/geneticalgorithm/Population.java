package de.fhws.geneticalgorithm;

import de.fhws.utility.ThrowingConsumer;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public final class Population<T extends Individual<T>> implements Serializable
{
	private List<T> individuals;

	private int generation;

	public Population()
	{
		this.individuals = new ArrayList<>();
	}

	public Population(List<T> individuals)
	{
		this.individuals = new ArrayList<>(individuals);
	}

	void calcFitnesses(Optional<ExecutorService> executor)
	{
		executor.ifPresentOrElse(exec ->
				individuals.stream()
					.map(individual -> exec.submit(individual::calcFitness))
					.forEach(ThrowingConsumer.unchecked(Future::get))

			, () -> individuals.forEach(Individual::calcFitness));
	}

	/**
	 * sorts the population in DESCENDING order.
	 * It uses the compareTo method of the solutions for this.
	 */
	public void sortPopByFitness()
	{
		Collections.sort(individuals);
		Collections.reverse(individuals);
	}

	public T getBest()
	{
		return Collections.max(individuals);
	}

	public double getAverageFitness()
	{
		return individuals.stream().mapToDouble(Individual::getFitness).average().getAsDouble();
	}

	public int getGeneration()
	{
		return generation;
	}

	public void incGeneration()
	{
		generation++;
	}

	public int getSize()
	{
		return individuals.size();
	}

	public void replaceAllIndividuals(Collection<Individual<T>> collection)
	{
		individuals.clear();
		//	collection.forEach(ind -> individuals.add(ind.copy()));
		individuals.addAll(collection.stream().map(ind -> ind.copy()).collect(Collectors.toList()));
	}

	public List<T> getIndividuals()
	{
		return individuals;
	}

	public void setIndividuals(List<T> individuals)
	{
		if (individuals == null)
			throw new NullPointerException("Individuals can't be null");
		this.individuals = individuals;
	}

}
