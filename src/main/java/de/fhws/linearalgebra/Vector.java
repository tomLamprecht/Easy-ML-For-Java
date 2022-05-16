package de.fhws.linearalgebra;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.DoubleStream;

public class Vector implements Serializable{
    private double[] data;


    /**
     * creates a vector with the given size. Initialized with 0.
     * @param size size of vector
     */
    public Vector(int size) {
        data = new double[size];
    }


    /**
     * creates a vector with a copy of the data.
     * @param data initial values
     */
    public Vector(double[] data) {
        if(data.length <= 0)
            throw new IllegalArgumentException("data length must be greater than 0");

        this.data = new double[data.length];
        System.arraycopy(data, 0, this.data, 0, data.length);
    }

    /**
     * randomizes this vector
     *
     * @param randomizer given randomizer, that holds the range
     */
    public void randomize(Randomizer randomizer) {
        for (int i = 0; i < data.length; i++) {
            data[i] = randomizer.getInRange();
        }
    }

    /**
     * adds the given vector to this vector
     *
     * @param other the vector which is added on this vector
     * @return the result of the addition
     */
    public Vector add(Vector other) {
        applyOperator(other, Double::sum);
        return this;
    }

    /**
     * subtracts the given vector from this vector
     *
     * @param other the vector which is subtracted from this vector
     * @return the result of the subtraction
     */
    public Vector sub(Vector other) {
        applyOperator(other, (d1, d2) -> d1 - d2);
        return this;
    }

    public void applyOperator(Vector other, DoubleBinaryOperator op) {
        if (other.size() != this.size())
            throw new IllegalArgumentException("vectors must be of the same length");

        for (int i = 0; i < data.length; i++) {
            data[i] = op.applyAsDouble(this.data[i], other.data[i]);
        }
    }

    /**
     * applies the DoubleUnaryOperator (Function with Double accepted and Double
     * returned) to this vector, on every value
     *
     * @param function function which is applied to every value of the vector
     * @return this vector, after the function was applied
     */
    public Vector apply(DoubleUnaryOperator function) {
        this.data = Arrays.stream(data).map(function).toArray();
        return this;
    }

    /**
     * finds the index of the biggest number in this vector
     *
     * @return the index of the biggest number in this vector
     */
    public int getIndexOfBiggest() {
        int index = 0;
        for (int i = 1; i < data.length; i++) {
            if (data[i] > data[index])
                index = i;
        }
        return index;
    }

    /**
     * get whole data (reference)
     * @return data reference
     */
    public double[] getData()
    {
        return data;
    }


    /**
     * gets data at index
     *
     * @param index index of desired data
     * @return data at index
     */
    public double get(int index) {
        return data[index];
    }

    /**
     * sets data at index
     *
     * @param index index of desired data
     */
    public void set(int index, double value) {
        data[index] = value;
    }

    /**
     * gets the size of the vector
     *
     * @return size of the vector
     */
    public int size() {
        return data.length;
    }

    /**
     * creates a copy of this vector
     * @return
     */
    public Vector copy() {
        return new Vector(this.data);
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < data.length; i++) {
            s += "| " + i + ": " + String.format("%.2f", data[i]) + " |\n";
        }
        return s;
    }
}
