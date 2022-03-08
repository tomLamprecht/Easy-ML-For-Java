package de.fhws.networks.convulotionalnn.convolution;

import java.util.ArrayList;
import java.util.List;

import de.fhws.linearalgebra.LinearAlgebra;
import de.fhws.linearalgebra.Matrix;
import de.fhws.networks.convulotionalnn.Filter;

public class ConvolutionPadding extends Convolution {

	public ConvolutionPadding(int filterSize, int filterAmount) {
		super(filterSize, filterAmount);
	}

	public List<Matrix> convolute(Matrix level) {
		// Usd for padding

		int puffer = (filterSize - 1);
		double[][] matrix = new double[level.getNumRows() + puffer * 2][level.getNumCols() + puffer * 2];
		for (int i = 0; i < level.getNumRows(); i++) {
			for (int j = 0; j < level.getNumCols(); j++) {
				matrix[i + puffer][j + puffer] = (Double) level.get(i, j);
			}
		}
		Matrix paddingLevel = new Matrix(matrix);

		List<Matrix> result = new ArrayList<>();
		for (Filter currentFilter : filter) {

			Matrix currentLevel = new Matrix(new double[paddingLevel.getNumRows()][paddingLevel.getNumCols()]);

			for (int i = 0; i < level.getNumRows(); i++) {
				for (int j = 0; j < level.getNumCols(); j++) {
					Matrix subMatrix = LinearAlgebra.generateSubMatrix(level, i, filterSize, j, filterSize);
					currentLevel.set(i, j, calcMatrices(subMatrix, currentFilter));
				}
			}
			result.add(currentLevel);
		}

		return result;
	}

}
