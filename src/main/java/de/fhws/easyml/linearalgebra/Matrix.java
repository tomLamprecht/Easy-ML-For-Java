package de.fhws.easyml.linearalgebra;

import de.fhws.easyml.utility.Validator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class Matrix implements Serializable {

    private double[][] data;

    /**
     * creates a matrix with the given size. Initialized with 0.
     * @param rows number of rows
     * @param cols number of columns
     */
    public Matrix(int rows, int cols) {
        if(rows == 0 || cols == 0)
            throw new IllegalArgumentException();
        this.data = new double[rows][cols];
        for(int i = 0; i < this.data.length; i++) {
            this.data[i] = new double[data[i].length];
        }
    }

    /**
     * creates a matrix with a copy of the given data
     * @param data given data
     */
    public Matrix(double[][] data) {
        if(data.length == 0 || data[0].length == 0)
            throw new IllegalArgumentException();

        this.data = new double[data.length][data[0].length];
        for(int i = 0; i < this.data.length; i++) {
            if(data[i].length != data[0].length)
                throw new IllegalArgumentException("all rows of the data must have the same length");
            System.arraycopy(data[i], 0, this.data[i], 0, this.data[i].length);
        }
    }

    /**
     * randomizes this matrix
     * @param randomizer given randomizer, that holds the range
     */
    public void randomize(Randomizer randomizer) {
        for (int i = 0; i < getNumRows(); i++) {
            for (int j = 0; j < getNumCols(); j++) {
                data[i][j] = randomizer.getInRange();
            }
        }
    }
    
    /**
     * applies the {@code func} on every element
     * @param func that should be applied
     */
    public void apply(DoubleUnaryOperator func) {
    	applyAndReturn(func);
    }


    /**
     * applies the {@code func} on every element and returns the Matrix
     * @param func that should be applied
     * @return the Matrix itself
     */
    public Matrix applyAndReturn(DoubleUnaryOperator func){
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = func.applyAsDouble(data[i][j]);
            }
        }
        return this;
    }

    
    /**
     * Gets the highest double of the data matrix.
     * @return the highest number
     */
	public double getHighestNumber() {
		double record = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				if(get(i,j) > record)
					record = get(i,j);
			}
		}
		return record;
	}

    /**
     * Creates a sub Matrix based on the given values.
     * If the frame for the subMatrix is out of bounds of the original Matrix, it will be filled up with 0.
     * Due to need of high performance, there is no check if parameters are valid. Use with caution!
     * @param rowFrom start row for the new Matrix
     * @param colFrom start row for the new Matrix
     * @param rowLen amount of rows of the new Matrix
     * @param colLen amount of columns for the Matrix
     * @return created sub Matrix
     */
    public Matrix getSubMatrix(int rowFrom, int colFrom, int rowLen, int colLen){
        var subMatrixData = new double[rowLen][colLen];
            for (int i = Math.max(rowFrom, 0); i < rowFrom + rowLen; i++)
                subMatrixData[i - rowFrom] = i < getNumRows() ? copyOfRangeWithNegativeStart(data[i], colFrom, colFrom + colLen) : new double[colLen];

        return new Matrix(subMatrixData);
    }

    private double[] copyOfRangeWithNegativeStart(double[] org, int from, int to){
        if(from >= 0)
            return Arrays.copyOfRange(org, from, to);
        double[] values = new double[to-from];
        System.arraycopy(org, 0, values, -from, to);
        return values;
    }

    public Matrix multiply(Matrix other){
        Validator.value(this.getNumCols()).isEqualToOrThrow(other.getNumRows());
        double[][] result = new double[getNumRows()][other.getNumCols()];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                result[row][col] = multiplyMatricesCell(this.getData(), other.getData(), row, col);
            }
        }

        return new Matrix(result);
    }

    private double multiplyMatricesCell(double[][] firstMatrix, double[][] secondMatrix, int row, int col) {
        double cell = 0;
        for (int i = 0; i < secondMatrix.length; i++) {
            cell += firstMatrix[row][i] * secondMatrix[i][col];
        }
        return cell;
    }

    /**
     * gets the double value in the specified row and column
     * @param row row of the value
     * @param col column of the value
     * @return the specified double value
     */
    public double get(int row, int col) {
        return data[row][col];
    }

    /**
     * sets the double value in the specified row and column
     * @param row row of the value
     * @param col column of the value
     */
    public void set(int row, int col, double value) {
        data[row][col] = value;
    }

    /**
     * gets the number of rows
     * @return number of rows
     */
    public int getNumRows() {
        return data.length;
    }

    /**
     * gets the number of columns
     * @return number of columns
     */
    public int getNumCols() {
        return data.length == 0 ? 0 : data[0].length;
    }


    /**
     * get the data of the Matrix
     * @return double[][] data
     */
    public double[][] getData(){return this.data;}

    public DoubleStream getDataStream() {
        return Arrays.stream(data).flatMapToDouble(Arrays::stream);
    }

    /**
     * creates a copy of this matrix
     * @return
     */
    public Matrix copy() {
        return new Matrix(this.data);
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass( ) != o.getClass( ) ) return false;
        Matrix matrix = ( Matrix ) o;
        return Arrays.equals( data, matrix.data );
    }

    @Override
    public int hashCode( ) {
        return Arrays.hashCode( data );
    }
}
