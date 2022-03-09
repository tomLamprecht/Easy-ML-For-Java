package de.fhws.networks.convulotionalnn.pooling;

import de.fhws.linearalgebra.LinearAlgebra;
import de.fhws.linearalgebra.Matrix;

public class MaxPooling extends Pooling {

	public MaxPooling(int size) {
		this.size = size;
	}

	@Override
	public Matrix pool(Matrix level) {
		int matrixWidth = (int) Math.ceil(level.getNumCols() / (double) size);
		int matrixHeight = (int) Math.ceil(level.getNumRows() / (double) size);
		double[][] matrix = new double[matrixHeight][matrixWidth];
		for (int i = 0; i < level.getNumRows(); i += size) {
			for (int j = 0; j < level.getNumCols(); j += size) {
				Matrix subMatrix = LinearAlgebra.generateSubMatrix(level, i, size, j, size);
				matrix[i / size][j / size] = subMatrix.getHighestNumber();
			}
		}
		return new Matrix(matrix);
	}

	@Override
	public MaxPooling copy() {
		return new MaxPooling(size);
	}
	
	

}
