package de.fhws.networks.convulotionalnn.convolution;

import java.util.ArrayList;
import java.util.List;

import de.fhws.linearalgebra.Matrix;
import de.fhws.linearalgebra.Randomizer;
import de.fhws.networks.convulotionalnn.Filter;

public abstract class Convolution {

	
	protected List<Filter> filter;
	
	protected int filterSize;
	protected int filterAmount;
	
	public Convolution(int filterSize, int filterAmount ) {

		this.filterSize = filterSize;
		this.filter = new ArrayList<>();
		this.filterAmount = filterAmount;
	}
	
	protected Convolution(List<Filter> filter, int filterSize, int filterAmount) {
		this.filter = filter;
		this.filterSize = filterSize;
		this.filterAmount = filterAmount;
	}

	
	
	public void generateRandomizedFilters(Randomizer randFilter, Randomizer randFilterBias) {
		for (int i = 0; i < filterAmount; i++) {
			this.filter.add(Filter.generateRandomFilter(filterSize, randFilter, randFilterBias));
		}
	}
	
	public abstract List<Matrix> convolute(Matrix level);
	
	public List<Filter> getFilter() {
		return this.filter;
	}
	
	protected static double calcMatrices(Matrix m1, Filter m2) {
		double result = 0;
		for (int i = 0; i < m1.getNumRows(); i++) {
			for (int j = 0; j < m1.getNumCols(); j++) {
				result += m1.get(i, j) * m2.get(i, j);
			}
		}
		return result - m2.getBias();
	}
	
	public abstract Convolution copy();
	
}
