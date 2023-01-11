package de.fhws.easyml.ai.neuralnetwork;


import de.fhws.easyml.linearalgebra.Vector;
import de.fhws.easyml.utility.StreamUtil;
import de.fhws.easyml.utility.Validator;
import de.fhws.easyml.linearalgebra.Randomizer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class NeuralNet implements Serializable {

    private static final long serialVersionUID = -5984131490435879432L;

    int inputSize;
    private final List<Layer> layers;

    private NeuralNet( int inputSize ) {
        this.inputSize = inputSize;
        layers = new ArrayList<>( );
    }

    private NeuralNet( int inputSize, List<Layer> layers ) {
        this.inputSize = inputSize;
        this.layers = layers;
    }

    /**
     * calculates the output based on the given input vector
     *
     * @param input vector with the input values; must be the size specified at
     *              built
     * @return the calculated output vector
     * @throws IllegalArgumentException if the size of {@code input} is not the
     *                                  specified one
     */
    public Vector calcOutput(Vector input ) {
        return calcAllLayer( input ).get( layers.size( ) - 1 );
    }

    /**
     * calculates the output based on the given input vector
     *
     * @param input vector with the input values; must be the size specified at
     *              built
     * @return all calculated vectors (hidden layers and output layer)
     * @throws IllegalArgumentException if the size of {@code input} is not the
     *                                  specified one
     */
    public List<Vector> calcAllLayer( Vector input ) {
        Validator.value( input.size() )
                .isEqualToOrThrow(
                        inputSize,
                        () -> new IllegalArgumentException( "the input vector must be of the same size as the first layer" )
                );

        return doCalcLayers( input );
    }

    private List<Vector> doCalcLayers( Vector input ) {
        final List<Vector> list = new ArrayList<>( layers.size( ) );
        list.add( input );

        StreamUtil.of( layers.stream( ) )
                .forEachIndexed( ( layer, i ) -> list.add( layer.calcActivation( list.get( i ) ) ) );

        list.remove( 0 );
        return list;
    }

    public NeuralNet randomize( Randomizer weightRand, Randomizer biasRand ) {
        layers.forEach( layer -> layer.randomize( weightRand, biasRand ) );

        return this;
    }


    public List<Layer> getLayers( ) {
        return layers;
    }

    /**
     * copy the current NeuralNet
     *
     * @return copy of the current NeuralNet
     */
    public NeuralNet copy( ) {
        final List<Layer> copiedLayers = new ArrayList<>( );

        layers.forEach( layer -> copiedLayers.add( layer.copy() ) );

        return new NeuralNet( this.inputSize, copiedLayers );
    }


    public static class Builder {
        private final int inputSize;
        private final int outputSize;
        private final List<Integer> layerSizes = new ArrayList<>( );
        private ActivationFunction activationFunction;
        private Randomizer weightRand = new Randomizer( -1, 1 );
        private Randomizer biasRand = new Randomizer( 0, 1 );
        private final AtomicBoolean isBuilt = new AtomicBoolean( false );

        /**
         * Constructor to create a Builder which is capable to build a NeuralNet
         *
         * @throws IllegalArgumentException if depth is less or equal 1 or if inputNodes
         *                                  is less than 1
         */
        public Builder( int inputSize, int outputSize ) {
            Validator.value( inputSize ).isPositiveOrThrow( );
            Validator.value( outputSize ).isPositiveOrThrow( );

            this.inputSize = inputSize;
            this.outputSize = outputSize;
            activationFunction = d -> ( 1 + Math.tanh( d / 2 ) ) / 2;
        }

        /**
         * set activation function of the neural network. Must be called before adding
         * any layers
         *
         * @param activationFunction ActivationFunction (Function that accepts Double and returns
         *              Double) to describe the activation function which is applied on
         *              every layer on calculation
         */
        public Builder withActivationFunction( ActivationFunction activationFunction ) {
            this.activationFunction = activationFunction;
            return this;
        }

        public Builder withWeightRandomizer( Randomizer weightRandomizer ) {
            this.weightRand = weightRandomizer;
            return this;
        }

        public Builder withBiasRandomizer( Randomizer biasRandomizer ) {
            this.biasRand = biasRandomizer;
            return this;
        }

        /**
         * adds a layer to the neural network
         *
         * @param sizeOfLayer the number of nodes of the added layer
         * @return this
         * @throws IllegalArgumentException if sizeOfLayer are 0 or smaller
         */
        public Builder addLayer( int sizeOfLayer ) {
            Validator.value( sizeOfLayer ).isPositiveOrThrow( );

            layerSizes.add( sizeOfLayer );

            return this;
        }

        /**
         * adds the specified amountOfToAddedLayers of layers to the neural network
         *
         * @param amountOfToAddedLayers the amountOfToAddedLayers of layers added
         * @param sizeOfLayers the number of nodes of the added layers
         * @return this
         */
        public Builder addLayers( int amountOfToAddedLayers, int sizeOfLayers ) {
            Validator.value( amountOfToAddedLayers ).isPositiveOrThrow( );

            IntStream.range( 0, amountOfToAddedLayers ).forEach( i -> addLayer( sizeOfLayers ) );

            return this;
        }

        /**
         * adds layers of the specified sizes to the neural network
         *
         * @param sizesOfLayers array of the number of nodes which are added
         * @return this
         */
        public Builder addLayers( int... sizesOfLayers ) {
            Arrays.stream( sizesOfLayers ).forEach( this::addLayer );

            return this;
        }

        /**
         * builds the NeuralNet
         *
         * @return the built NeuralNet
         * @throws IllegalStateException
         */
        public NeuralNet build( ) {
            if (isBuilt.getAndSet( true ))
                throw new IllegalStateException( "this builder has already been used for building" );

            layerSizes.add( outputSize );

            NeuralNet nn = new NeuralNet( inputSize );

            StreamUtil.of( layerSizes.stream() )
                    .forEachWithBefore( inputSize, ( current, before ) ->
                            nn.layers.add( new Layer( current, before, activationFunction ) ));

            return nn.randomize( weightRand, biasRand );
        }

    }

}
