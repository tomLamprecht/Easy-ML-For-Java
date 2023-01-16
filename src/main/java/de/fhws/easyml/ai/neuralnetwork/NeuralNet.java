package de.fhws.easyml.ai.neuralnetwork;


import de.fhws.easyml.ai.neuralnetwork.activationfunction.ActivationFunction;
import de.fhws.easyml.ai.neuralnetwork.activationfunction.Sigmoid;
import de.fhws.easyml.ai.neuralnetwork.costfunction.CostFunction;
import de.fhws.easyml.linearalgebra.Matrix;
import de.fhws.easyml.linearalgebra.Vector;
import de.fhws.easyml.utility.StreamUtil;
import de.fhws.easyml.utility.Validator;
import de.fhws.easyml.linearalgebra.Randomizer;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class NeuralNet implements Serializable {

    private static final long serialVersionUID = -5984131490435879432L;

    int inputSize;
    private final List<Layer> layers;

    private NeuralNet( int inputSize ) {
        this.inputSize = inputSize;
        layers = new ArrayList<>();
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
    public Vector calcOutput( Vector input ) {
        return calcAllLayer( input ).get( layers.size() - 1 );
    }

    public void trainBatch( List<Vector> inputs, List<Vector> expectedOutputs, CostFunction costFunction, double learningRate ) {
        Validator.value( inputs.size() ).isEqualToOrThrow( expectedOutputs.size() );
        Validator.value( learningRate ).isBetweenOrThrow( 0, 1 );

        List<Map<Integer, Matrix>> gradientsList = createGradientsFromBatch( inputs, expectedOutputs, costFunction );

        Map<Integer, Matrix> resultGradients = getAveragedGradients( gradientsList );

        updateLayers( learningRate, resultGradients );
    }

    @NotNull
    private List<Map<Integer, Matrix>> createGradientsFromBatch( List<Vector> inputs, List<Vector> expectedOutputs, CostFunction costFunction ) {
        List<Map<Integer, Matrix>> gradientsList = new ArrayList<>();
        for ( int i = 0; i < inputs.size(); i++ ) {
            gradientsList.add( calcGradients( inputs.get( i ), expectedOutputs.get( i ), costFunction ) );

        }
        return gradientsList;
    }

    private void updateLayers( double learningRate, Map<Integer, Matrix> resultGradients ) {
        for ( int i = 0; i < layers.size(); i++ )
            updateLayer( layers.get( i ).getWeights(), learningRate, resultGradients.get( i ) );
    }

    private void updateLayer( Matrix cur, double learningRate, Matrix gradients ) {
        for ( int row = 0; row < cur.getNumRows(); row++ ) {
            for ( int col = 0; col < cur.getNumCols(); col++ ) {
                cur.set( row, col, cur.get( row, col ) - learningRate * gradients.get( row, col ) );
            }
        }
    }

    private Map<Integer, Matrix> getAveragedGradients( List<Map<Integer, Matrix>> gradientsList ) {
        Map<Integer, Matrix> resultGradients = createEmptyResultGradientMap( gradientsList );

        sumUpGradients( gradientsList, resultGradients );

        averageOutGradients( gradientsList, resultGradients );


        return resultGradients;
    }

    private void sumUpGradients( List<Map<Integer, Matrix>> gradientsList, Map<Integer, Matrix> resultGradients ) {
        for ( Map<Integer, Matrix> gradients : gradientsList ) {
            addGradientsToResult( resultGradients, gradients );
        }
    }

    private void averageOutGradients( List<Map<Integer, Matrix>> gradientsList, Map<Integer, Matrix> resultGradients ) {
        resultGradients.forEach( ( i, m ) -> m.apply( v -> v / gradientsList.size() ) );
    }

    private void addGradientsToResult( Map<Integer, Matrix> resultGradients, Map<Integer, Matrix> gradients ) {
        for ( Map.Entry<Integer, Matrix> entry : gradients.entrySet() ) {
            Matrix resultMatrix = resultGradients.get( entry.getKey() );
            addEachGradientToResultMatrix( entry.getValue(), resultMatrix );
        }
    }

    private void addEachGradientToResultMatrix( Matrix gradients, Matrix resultMatrix ) {
        for ( int i = 0; i < resultMatrix.getNumRows(); i++ ) {
            for ( int j = 0; j < resultMatrix.getNumCols(); j++ ) {
                resultMatrix.set( i, j, resultMatrix.get( i, j ) + gradients.get( i, j ) );
            }
        }
    }

    private Map<Integer, Matrix> createEmptyResultGradientMap( List<Map<Integer, Matrix>> gradientsList ) {
        Map<Integer, Matrix> resultGradients = new HashMap<>();
        for ( int i = 0; i < gradientsList.get( 0 ).size(); i++ ) {
            resultGradients.put( i, new Matrix( gradientsList.get( 0 ).get( i ).getNumRows(), gradientsList.get( 0 ).get( i ).getNumCols() ) );
        }
        return resultGradients;
    }

    private Map<Integer, Matrix> calcGradients( Vector input, Vector expectedOutput, CostFunction costFunction ) {
        validateInputVector( input );

        Map<Integer, Layer.TrainingResult> layerIndexToTrainingResult = feedForward( input );
        return backpropagate( expectedOutput, costFunction, layerIndexToTrainingResult, input );
    }

    private Map<Integer, Matrix> backpropagate( Vector expectedOutput, CostFunction costFunction, Map<Integer, Layer.TrainingResult> layerIndexToTrainingResult, Vector input ) {
        Map<Integer, Matrix> layerIndexToGradient = new HashMap<>();
        for ( int i = layers.size() - 1; i >= 0; i-- ) {
            calculateDerivativesOfCostFunction( i, expectedOutput, costFunction, layerIndexToTrainingResult );
            layerIndexToGradient.put( i, weightGradientsForEachWeightOfLayer( layerIndexToTrainingResult, i, layers.get( i ), input ) );
        }
        return layerIndexToGradient;
    }

    private Matrix weightGradientsForEachWeightOfLayer( Map<Integer, Layer.TrainingResult> layerIndexToTrainingResult, int i, Layer layer, Vector input ) {
        Matrix gradients = new Matrix( layer.getWeights().getNumRows(), layer.getWeights().getNumCols() );
        for ( int j = 0; j < gradients.getNumRows(); j++ ) {
            for ( int k = 0; k < gradients.getNumCols(); k++ ) {
                double neuronKOfPrevLayer = i > 0 ? layerIndexToTrainingResult.get( i - 1 ).getOutputWithActivationFunction().get( k ) : input.get( k );
                double dervOfActivationFunction = layer.getFActivation().derivative( layerIndexToTrainingResult.get( i ).getOutputStripped().get( j ) );
                double dervOfCostFunction = layerIndexToTrainingResult.get( i ).getDervOfCostFunction().get( j );
                double value = neuronKOfPrevLayer * dervOfActivationFunction * dervOfCostFunction;
                gradients.set( j, k, value );
            }
        }
        return gradients;
    }

    private void calculateDerivativesOfCostFunction( int indexOfLayer, Vector expectedOutput, CostFunction costFunction, Map<Integer, Layer.TrainingResult> layerIndexToTrainingResult ) {
        if ( indexOfLayer == layers.size() - 1 )
            calculateCostFunctionDerivForLastLayer( expectedOutput, costFunction, layerIndexToTrainingResult );
        else
            calculateCostFunctionDerivForAnyLayer( layerIndexToTrainingResult.get( indexOfLayer ), layers.get( indexOfLayer + 1 ), layerIndexToTrainingResult.get( indexOfLayer + 1 ) );
    }

    private void calculateCostFunctionDerivForAnyLayer( Layer.TrainingResult current, Layer followingLayer, Layer.TrainingResult followingTrainingResult ) {
        current.setDervOfCostFunction( calcCostFunctionDerivative( current, followingLayer, followingTrainingResult ) );
    }

    private void calculateCostFunctionDerivForLastLayer( Vector expectedOutput, CostFunction costFunction, Map<Integer, Layer.TrainingResult> layerIndexToTrainingResult ) {
        Vector outputVector = layerIndexToTrainingResult.get( layers.size() - 1 ).getOutputWithActivationFunction();
        Vector dervOfOutput = new Vector( outputVector.size() );
        for ( int i = 0; i < layers.get( layers.size() - 1 ).size(); i++ ) {
            dervOfOutput.set( i, costFunction.derivativeWithRespectToNeuron( expectedOutput, outputVector, i ) );
        }
        layerIndexToTrainingResult.get( layers.size() - 1 ).setDervOfCostFunction( dervOfOutput );
    }

    private Vector calcCostFunctionDerivative( Layer.TrainingResult trainingResultCurrentLayer, Layer followingLayer, Layer.TrainingResult trainingResultFollowingLayer ) {

        Vector derivatives = new Vector( trainingResultCurrentLayer.size() );
        for ( int i = 0; i < trainingResultCurrentLayer.size(); i++ ) {
            derivatives.set( i, calcDervOfNeuron( followingLayer, trainingResultFollowingLayer, i ) );
        }
        return derivatives;
    }

    private double calcDervOfNeuron( Layer followingLayer, Layer.TrainingResult trainingResultFollowingLayer, int neuronIndex ) {
        double temp = 0;
        for ( int i = 0; i < followingLayer.size(); i++ ) {
            double weightFromNeuronIndexToI = followingLayer.getWeights().get( i, neuronIndex );
            double dervOfActivationFunctionWithOutputStripped = doDervOfActivationFuncOfLayer( followingLayer, trainingResultFollowingLayer, i );
            double dervOfCostFunctionOfNeuronI = trainingResultFollowingLayer.getDervOfCostFunction().get( i );
            temp = weightFromNeuronIndexToI * dervOfActivationFunctionWithOutputStripped * dervOfCostFunctionOfNeuronI;

        }
        return temp;
    }

    private double doDervOfActivationFuncOfLayer( Layer layer, Layer.TrainingResult trainingResult, int i ) {
        return layer.getFActivation().derivative( trainingResult.getOutputStripped().get( i ) );
    }

    private Map<Integer, Layer.TrainingResult> feedForward( Vector input ) {
        Map<Integer, Layer.TrainingResult> trainingResults = new HashMap<>();

        for ( int i = 0; i < layers.size(); i++ ) {
            Layer.TrainingResult result = layers.get( i ).feedForward( input );
            trainingResults.put( i, result );
            input = result.getOutputWithActivationFunction();
        }

        return trainingResults;
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
        validateInputVector( input );

        return doCalcLayers( input );
    }

    private void validateInputVector( Vector input ) {
        Validator.value( input.size() )
                .isEqualToOrThrow(
                        inputSize,
                        new IllegalArgumentException( "the input vector must be of the same size as the first layer" )
                );
    }

    private List<Vector> doCalcLayers( Vector input ) {
        final List<Vector> list = new ArrayList<>( layers.size() );
        list.add( input );

        StreamUtil.of( layers.stream() )
                .forEachIndexed( ( layer, i ) -> list.add( layer.calcActivation( list.get( i ) ) ) );

        list.remove( 0 );
        return list;
    }

    public NeuralNet randomize( Randomizer weightRand, Randomizer biasRand ) {
        layers.forEach( layer -> layer.randomize( weightRand, biasRand ) );

        return this;
    }


    public List<Layer> getLayers() {
        return layers;
    }

    /**
     * copy the current NeuralNet
     *
     * @return copy of the current NeuralNet
     */
    public NeuralNet copy() {
        final List<Layer> copiedLayers = new ArrayList<>();

        layers.forEach( layer -> copiedLayers.add( layer.copy() ) );

        return new NeuralNet( this.inputSize, copiedLayers );
    }


    public static class Builder {
        private final int inputSize;
        private final int outputSize;
        private final List<Integer> layerSizes = new ArrayList<>();
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
            Validator.value( inputSize ).isPositiveOrThrow();
            Validator.value( outputSize ).isPositiveOrThrow();

            this.inputSize = inputSize;
            this.outputSize = outputSize;
            activationFunction = new Sigmoid();
        }

        /**
         * set activation function of the neural network. Must be called before adding
         * any layers
         *
         * @param activationFunction ActivationFunction (Function that accepts Double and returns
         *                           Double) to describe the activation function which is applied on
         *                           every layer on calculation
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
            Validator.value( sizeOfLayer ).isPositiveOrThrow();

            layerSizes.add( sizeOfLayer );

            return this;
        }

        /**
         * adds the specified amountOfToAddedLayers of layers to the neural network
         *
         * @param amountOfToAddedLayers the amountOfToAddedLayers of layers added
         * @param sizeOfLayers          the number of nodes of the added layers
         * @return this
         */
        public Builder addLayers( int amountOfToAddedLayers, int sizeOfLayers ) {
            Validator.value( amountOfToAddedLayers ).isPositiveOrThrow();

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
        public NeuralNet build() {
            if ( isBuilt.getAndSet( true ) )
                throw new IllegalStateException( "this builder has already been used for building" );

            layerSizes.add( outputSize );

            NeuralNet nn = new NeuralNet( inputSize );

            StreamUtil.of( layerSizes.stream() )
                    .forEachWithBefore( inputSize, ( current, before ) ->
                            nn.layers.add( new Layer( current, before, activationFunction ) ) );

            return nn.randomize( weightRand, biasRand );
        }

    }

}
