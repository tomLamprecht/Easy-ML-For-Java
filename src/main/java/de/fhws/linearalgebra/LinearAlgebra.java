package de.fhws.linearalgebra;

import java.util.function.DoubleUnaryOperator;


public class LinearAlgebra {

    public static Vector add(Vector v1, Vector v2) {
        Vector res = v1.copy();
        res.add(v2);
        return res;
    }

    public static Vector sub(Vector v1, Vector v2) {
        Vector res = v1.copy();
        res.sub(v2);
        return res;
    }

    public static Vector apply(Vector vector, DoubleUnaryOperator func) {
        Vector res = vector.copy();
        res.apply(func);
        return res;
    }

    public static Vector multiply(Matrix matrix, Vector vector) {
        if(vector.size() != matrix.getNumCols())
            throw new IllegalArgumentException("vector must have the same size as the matrix has columns");
        Vector res = new Vector(matrix.getNumRows());
        for(int i = 0; i < matrix.getNumRows(); i++) {
            double sum = 0;
            for(int j = 0; j < matrix.getData()[i].length; j++) {
                sum += matrix.getData()[i][j] * vector.get(j);
            }
            res.set(i, sum);
        }
        return res;
    }
    
	public static Matrix generateSubMatrix(Matrix origin, int row, int height, int col, int width) {
		if(row < 0 || width <= 0 || col < 0 || height <= 0)
			throw new IllegalArgumentException("row or col were smaller than 0 or width and height weren't greater than 0");
		if(row > origin.getNumRows() || col > origin.getNumCols())
			throw new IllegalArgumentException("row and col should be smaller than width/height");
		
		double[][] newMatrix = new double[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				try {
					newMatrix[i][j] = origin.get(i + row, j + col);
				} catch (IndexOutOfBoundsException e) {
					newMatrix[i][j] = 0;
				}
			}
		}
		return new Matrix(newMatrix);
	}


}
