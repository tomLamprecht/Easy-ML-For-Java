package de.fhws.linearalgebra;

import java.util.Arrays;

public class Matrix {

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
            Arrays.fill(this.data[i], 0);
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
     * @param range the range in which the random numbers should be (abs from 0)
     * @param negative if {@code true} the numbers will also be negative (but always > -range)
     */
    public void randomize( double range, boolean negative) {
        for (int i = 0; i < getNumRows(); i++) {
            for (int j = 0; j < getNumCols(); j++) {
                double value = Math.random() * range;
                if(negative && Math.random() < 0.5)
                    value *= -1;
                data[i][j] = value;
            }
        }
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

    /**
     * creates a copy of this matrix
     * @return
     */
    public Matrix copy() {
        return new Matrix(this.data);
    }
}
