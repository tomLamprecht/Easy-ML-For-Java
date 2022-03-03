package de.fhws.geneticalgorithm;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Population <T extends Solution> implements Serializable {
	private List<T> solutions;

	private int generation;
	
	void calcFitnesses() {
		solutions.forEach(Solution::calcFitness);
	}
	
	/**
	 * sorts the population in DESCANDING order.
	 * It uses the compareTo method of the solutions for this.
	 */
	public void sortPopByFitness() {
		Collections.sort(solutions);
		Collections.reverse(solutions);
	}
	
	public int getSize() {
		return solutions.size();
	}
	
	public T getBest() {
		return Collections.max(solutions);
	}
	
	public double getAverageFitness() {
		return solutions.stream().mapToDouble(Solution::getFitness).average().getAsDouble();
	}
	
	public int getGeneration() {
		return generation;
	}
	
	public void incGeneration() {
		generation++;
	}
	
	public List<T> getSolutions() {
		return solutions;
	}
	
	public void setSolutions(List<T> solutions) {
		if(solutions == null)
			throw new NullPointerException("solutions can't be null");
		this.solutions = solutions;
	}
	
}
