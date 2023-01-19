package de.fhws.easyml.ai.neuralnetwork;

import de.fhws.easyml.ai.neuralnetwork.costfunction.CostFunction;
import de.fhws.easyml.linearalgebra.Matrix;
import de.fhws.easyml.linearalgebra.Vector;
import de.fhws.easyml.utility.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Backpropagation {

    private final NeuralNet nn;

    public Backpropagation(NeuralNet neuralNet) {
        this.nn = neuralNet;
    }

    public void trainBatch(List<Vector> inputs, List<Vector> expectedOutputs, CostFunction costFunction, double learningRate ) {
        Validator.value( inputs.size() ).isEqualToOrThrow( expectedOutputs.size() );
        Validator.value( learningRate ).isBetweenOrThrow( 0, 1 );

        List<Map<Integer, Matrix>> gradientsList = createGradientsFromBatch( inputs, expectedOutputs, costFunction );

        Map<Integer, Matrix> resultGradients = getAveragedGradients( gradientsList );

        updateLayers( learningRate, resultGradients );
    }

    private List<Map<Integer, Matrix>> createGradientsFromBatch( List<Vector> inputs, List<Vector> expectedOutputs, CostFunction costFunction ) {
        List<Map<Integer, Matrix>> gradientsList = new ArrayList<>();
        for ( int i = 0; i < inputs.size(); i++ ) {
            gradientsList.add( calcGradients( inputs.get( i ), expectedOutputs.get( i ), costFunction ) );

        }
        return gradientsList;
    }

    private void updateLayers( double learningRate, Map<Integer, Matrix> resultGradients ) {
        for ( int i = 0; i < nn.layers.size(); i++ )
            updateLayer( nn.layers.get( i ).getWeights(), learningRate, resultGradients.get( i ) );
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
        nn.validateInputVector( input );

        Map<Integer, Layer.TrainingResult> layerIndexToTrainingResult = feedForward( input );
        return backpropagate( expectedOutput, costFunction, layerIndexToTrainingResult, input );
    }

    private Map<Integer, Matrix> backpropagate( Vector expectedOutput, CostFunction costFunction, Map<Integer, Layer.TrainingResult> layerIndexToTrainingResult, Vector input ) {
        Map<Integer, Matrix> layerIndexToGradient = new HashMap<>();
        for ( int i = nn.layers.size() - 1; i >= 0; i-- ) {
            calculateDerivativesOfCostFunction( i, expectedOutput, costFunction, layerIndexToTrainingResult );
            layerIndexToGradient.put( i, weightGradientsForEachWeightOfLayer( layerIndexToTrainingResult, i, nn.layers.get( i ), input ) );
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
        if ( indexOfLayer == nn.layers.size() - 1 )
            calculateCostFunctionDerivForLastLayer( expectedOutput, costFunction, layerIndexToTrainingResult );
        else
            calculateCostFunctionDerivForAnyLayer( layerIndexToTrainingResult.get( indexOfLayer ), nn.layers.get( indexOfLayer + 1 ), layerIndexToTrainingResult.get( indexOfLayer + 1 ) );
    }

    private void calculateCostFunctionDerivForAnyLayer( Layer.TrainingResult current, Layer followingLayer, Layer.TrainingResult followingTrainingResult ) {
        current.setDervOfCostFunction( calcCostFunctionDerivative( current, followingLayer, followingTrainingResult ) );
    }

    private void calculateCostFunctionDerivForLastLayer( Vector expectedOutput, CostFunction costFunction, Map<Integer, Layer.TrainingResult> layerIndexToTrainingResult ) {
        Vector outputVector = layerIndexToTrainingResult.get( nn.layers.size() - 1 ).getOutputWithActivationFunction();
        Vector dervOfOutput = new Vector( outputVector.size() );
        for ( int i = 0; i < nn.layers.get( nn.layers.size() - 1 ).size(); i++ ) {
            dervOfOutput.set( i, costFunction.derivativeWithRespectToNeuron( expectedOutput, outputVector, i ) );
        }
        layerIndexToTrainingResult.get( nn.layers.size() - 1 ).setDervOfCostFunction( dervOfOutput );
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

        for ( int i = 0; i < nn.layers.size(); i++ ) {
            Layer.TrainingResult result = nn.layers.get( i ).feedForward( input );
            trainingResults.put( i, result );
            input = result.getOutputWithActivationFunction();
        }

        return trainingResults;
    }

}
