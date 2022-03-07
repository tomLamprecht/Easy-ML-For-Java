package de.fhws.geneticalgorithm;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public final class Population <T extends Individual<T>> implements Serializable {
	private List<T> individuals;

	private int generation;
	
	void calcFitnesses() {
		individuals.forEach(Individual::calcFitness);
	}
	
	/**
	 * sorts the population in DESCANDING order.
	 * It uses the compareTo method of the solutions for this.
	 */
	public void sortPopByFitness() {
		Collections.sort(individuals);
		Collections.reverse(individuals);
	}
	
	public T getBest() {
		return Collections.max(individuals);
	}

	public double getAverageFitness() {
		return individuals.stream().mapToDouble(Individual::getFitness).average().getAsDouble();
	}

	public int getGeneration() {
		return generation;
	}

	public void incGeneration() {
		generation++;
	}

	public int getSize() {
		return individuals.size();
	}

	public List<T> getIndividuals() {
		return individuals;
	}

	public void setIndividuals(List<T> individuals) {
		if(individuals == null)
			throw new NullPointerException("solutions can't be null");
		this.individuals = individuals;
	}
	
}
