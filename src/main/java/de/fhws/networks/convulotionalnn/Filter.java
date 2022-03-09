package de.fhws.networks.convulotionalnn;

import de.fhws.linearalgebra.Matrix;
import de.fhws.linearalgebra.Randomizer;

public class Filter {

	private Matrix filter;
	private double bias;
	
	/*
	 * Creates a Filter that has a quadratic Matrix underneath thats initialized with 0
	 * and a bias of 0
	 */
	public Filter(int size) {
		this.filter = new Matrix(size, size);
	}
	
	public static Filter generateRandomFilter(int size, Randomizer rand, Randomizer randBias) {
		Filter filter = new Filter(size);
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				filter.filter.set(i, j, rand.getInRange());
			}
		}
		filter.bias = randBias.getInRange();
		return filter;
	}
	
	public double get(int row, int col) {
		return filter.get(row, col);
	}
	
	public Matrix getFilter(){
		return this.filter;
	}
	
	public double getBias() {
		return this.bias;
	}
	
	@Override
	public String toString() {
		String result = "";
		for(int i = 0; i < filter.getNumRows(); i ++) {
			for (int j = 0; j < filter.getNumCols(); j++) {
				result += filter.get(i, j) + " ";
			}
			result += "\n";
		}
		
		return result;
	}
	
}
