package de.fhws.easyml.ai.neuralnetwork;


import de.fhws.easyml.linearalgebra.Vector;
import de.fhws.easyml.utility.Validator;
import de.fhws.easyml.linearalgebra.LinearAlgebra;
import de.fhws.easyml.linearalgebra.Matrix;
import de.fhws.easyml.linearalgebra.Randomizer;

import java.io.Serializable;

public class Layer implements Serializable {

    private static final long serialVersionUID = -3844443062431620792L;

    private final Matrix weights;
    private final Vector bias;
    private final ActivationFunction fActivation;

    Layer( int size, int sizeOfLayerBefore, ActivationFunction activationFunction ) {
        Validator.value( size ).isPositiveOrThrow( );
        Validator.value( sizeOfLayerBefore ).isPositiveOrThrow( );

        weights = new Matrix( size, sizeOfLayerBefore );
        bias = new Vector( size );

        this.fActivation = activationFunction;
    }

    /**
     * Copy Constructor
     */
    private Layer( Layer copy ) {
        this.weights = new Matrix( copy.weights.getData( ) );
        this.bias = new Vector( copy.bias.getData( ) );
        this.fActivation = copy.fActivation;
    }

    /**
     * calculates the activation of this layer, based on the given activation of the linked layer
     *
     * @param activationsOfLayerBefore layer on which the activation is based
     * @return a Vector with the activation of this layer as a vector
     * @throws IllegalArgumentException if the number of columns of the weights does not fit to the size of activationsOfLayerBefore
     */
    public Vector calcActivation( Vector activationsOfLayerBefore ) {
        Validator.value( weights.getNumCols( ) )
                .isEqualToOrThrow(
                        activationsOfLayerBefore.size( ),
                        () -> new IllegalArgumentException( "size of activationsOfLayerBefore must fit with weights columns" )
                );

        return LinearAlgebra.multiply( weights, activationsOfLayerBefore ).sub( bias ).apply( fActivation );
    }

    public void randomize( Randomizer weightRand, Randomizer biasRand ) {
        weights.randomize( weightRand );
        bias.randomize( biasRand );
    }

    /**
     * gets the number of the nodes in this layer
     *
     * @return number of nodes in this layer
     */
    public int size( ) {
        return bias.size( );
    }

    /**
     * gets the Matrix of weights of this layer
     *
     * @return Matrix of weights in this layer
     */
    public Matrix getWeights( ) {
        return weights;
    }

    public Vector getBias( ) {
        return this.bias;
    }

    public Layer copy( ) {
        return new Layer( this );
    }

}
