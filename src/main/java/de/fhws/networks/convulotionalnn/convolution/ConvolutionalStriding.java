package de.fhws.networks.convulotionalnn.convolution;

import java.util.ArrayList;
import java.util.List;

import de.fhws.linearalgebra.LinearAlgebra;
import de.fhws.linearalgebra.Matrix;
import de.fhws.networks.convulotionalnn.Filter;

public class ConvolutionalStriding extends Convolution{

	int striding;
	
	public ConvolutionalStriding(int filterSize, int filterAmount, int striding) {
		super(filterSize, filterAmount);
		this.striding = striding;
	}
	
	private ConvolutionalStriding(List<Filter> filter, int filterSize, int filterAmount, int striding) {
		super(filter, filterSize, filterAmount);
		this.striding = striding;
	}
	
	@Override
	public List<Matrix> convolute(Matrix level) {
	
		List<Matrix> result = new ArrayList<>();
		for (Filter currentFilter : filter) {
			int newWidth =(int)Math.ceil(level.getNumCols()*1d/striding);
			int newHeight = (int)Math.ceil(level.getNumRows()*1d/striding);
			Matrix currentLevel = new Matrix(new double[newHeight][newWidth]);
			for (int i = 0; i < level.getNumRows(); i+= striding) {
				for (int j = 0; j < level.getNumCols(); j+= striding) {
					Matrix subMatrix = LinearAlgebra.generateSubMatrix(level, i, filterSize, j, filterSize);
					currentLevel.set(i/striding, j/striding, calcMatrices(subMatrix, currentFilter));
				}
			}
			result.add(currentLevel);
		}
	
		return result;
	}

	@Override
	public ConvolutionalStriding copy() {
		List<Filter> filterCopies = new ArrayList<>(filter.size());
		filter.forEach(f -> filterCopies.add(f.copy()));
		return new ConvolutionalStriding(filterCopies, this.filterSize, this.filterAmount, this.striding);
	}

}
