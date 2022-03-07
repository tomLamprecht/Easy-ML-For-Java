package de.fhws.networks.neuralnetworks;


import de.fhws.linearalgebra.LinearAlgebra;
import de.fhws.linearalgebra.Matrix;
import de.fhws.linearalgebra.Randomizer;
import de.fhws.linearalgebra.Vector;
import de.fhws.networks.ActivationFunction;

import java.io.Serial;
import java.io.Serializable;

public class Layer implements Serializable{

    @Serial
    private static final long serialVersionUID = -3844443062431620792L;

    private final Matrix weights;
    private final Vector bias;
    private final ActivationFunction fActivation;

    public Layer(int n, int linkedN, ActivationFunction activationFunction) {
        if(n < 1 || linkedN < 1)
            throw new IllegalArgumentException("n and linkedN must be greater than 0");
        weights = new Matrix(n, linkedN);
        bias = new Vector(n);
        this.fActivation = activationFunction;
    }

    /**
     * Copy Constructor
     * @return a copy of the given Layer
     */
    private Layer(Layer copy) {
        this.weights = new Matrix(copy.weights.getData());
        this.bias = new Vector(copy.bias.getData());
        this.fActivation = copy.fActivation;
    }

    /**
     * calculates the activation of this layer, based on the given activation of the linked layer
     * @param linkedActivation layer on which the activation is based
     * @return a Vector with the activation of this layer as a vector
     * @throws IllegalArgumentException if the number of columns of the weights does not fit to the size of linkedActivation
     */
    public Vector calcActivation(Vector linkedActivation) {
        if(weights.getNumCols() != linkedActivation.size())
            throw new IllegalArgumentException("size of linkedActivation does not fit with weights columns");
        return LinearAlgebra.multiply(weights, linkedActivation).sub(bias).apply(fActivation);
    }

    public void randomize(Randomizer weightRand, Randomizer biasRand) {
        weights.randomize(weightRand);
        bias.randomize(biasRand);
    }

    /**
     * gets the number of the nodes in this layer
     * @return number of nodes in this layer
     */
    public int getNumNodes() {
        return bias.size();
    }

    /**
     * gets the Matrix of weights of this layer
     * @return Matrix of weights in this layer
     */
    public Matrix getWeights() { return weights;}

    public Vector getBias() { return this.bias;}

    public Layer copy() {
        return new Layer(this);

    }

}
