package de.fhws.easyml.ai.neuralnetwork;

import de.fhws.easyml.ai.neuralnetwork.costfunction.CostFunction;
import de.fhws.easyml.linearalgebra.ApplyAble;
import de.fhws.easyml.linearalgebra.Matrix;
import de.fhws.easyml.linearalgebra.Vector;
import de.fhws.easyml.utility.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class Backpropagation {

    private final NeuralNet nn;

    protected Backpropagation(NeuralNet neuralNet) {
        this.nn = neuralNet;
    }

    protected void trainBatch(List<Vector> inputs, List<Vector> expectedOutputs, CostFunction costFunction, double learningRate ) {
        Validator.value( inputs.size() ).isEqualToOrThrow( expectedOutputs.size() );
        Validator.value( learningRate ).isBetweenOrThrow( 0, 1 );

        List<Map<Integer, GradientsObject>> gradientsList = createGradientsFromBatch( inputs, expectedOutputs, costFunction );

        Map<Integer, GradientsObject> resultGradients = getAveragedGradients( gradientsList );

        updateLayers( learningRate, resultGradients );
    }

    private List<Map<Integer, GradientsObject>> createGradientsFromBatch(List<Vector> inputs, List<Vector> expectedOutputs, CostFunction costFunction ) {
        List<Map<Integer, GradientsObject>> gradientsList = new ArrayList<>();
        for ( int i = 0; i < inputs.size(); i++ ) {
            gradientsList.add( calcGradients( inputs.get( i ), expectedOutputs.get( i ), costFunction ) );
        }
        return gradientsList;
    }

    private void updateLayers( double learningRate, Map<Integer, GradientsObject> resultGradients ) {
        for ( int i = 0; i < nn.layers.size(); i++ ) {
            updateLayer(learningRate, resultGradients.get(i), i);
        }
    }

    private void updateLayer(double learningRate, GradientsObject resultGradients, int layerIndex) {
        updateLayerWeights(nn.layers.get(layerIndex).getWeights(), learningRate, resultGradients.weightGradients);
        updateLayerBiases(nn.layers.get(layerIndex).getBias(), learningRate, resultGradients.biasGradients);
    }

    private void updateLayerBiases(Vector cur, double learningRate, Vector gradients){
        for (int i = 0; i < cur.size(); i++)
            cur.set(i, cur.get( i ) - learningRate * gradients.get( i ) );
    }

    private void updateLayerWeights(Matrix cur, double learningRate, Matrix gradients ) {
        for ( int row = 0; row < cur.getNumRows(); row++ ) {
            for ( int col = 0; col < cur.getNumCols(); col++ ) {
                cur.set( row, col, cur.get( row, col ) - learningRate * gradients.get( row, col ) );
            }
        }
    }

    private Map<Integer, GradientsObject> getAveragedGradients( List<Map<Integer, GradientsObject>> gradientsList ) {
        Map<Integer, GradientsObject> resultGradients = createEmptyResultGradientMap( gradientsList );

        sumUpGradients( gradientsList, resultGradients );

        averageOutGradients( gradientsList, resultGradients );

        return resultGradients;
    }

    private void sumUpGradients( List<Map<Integer, GradientsObject>> gradientsList, Map<Integer, GradientsObject> resultGradients ) {
        for ( Map<Integer, GradientsObject> gradients : gradientsList ) {
            addGradientsToResult( resultGradients, gradients );
        }
    }

    private void averageOutGradients( List<Map<Integer, GradientsObject>> gradientsList, Map<Integer, GradientsObject> resultGradients ) {
        resultGradients.values()
                .stream()
                .flatMap(e -> Stream.<ApplyAble<?>>of(e.weightGradients, e.biasGradients))
                .forEach(a -> a.apply(v -> v / gradientsList.size() ) );
    }

    private void addGradientsToResult( Map<Integer, GradientsObject> resultGradients, Map<Integer, GradientsObject> gradients ) {
        for ( Map.Entry<Integer, GradientsObject> entry : gradients.entrySet() ) {
            addWeightGradientsToResult(resultGradients, entry);
            addBiasGradientsToResult(resultGradients, entry);
        }
    }

    private void addBiasGradientsToResult(Map<Integer, GradientsObject> resultGradients, Map.Entry<Integer, GradientsObject> entry) {
        Vector resultVector = resultGradients.get( entry.getKey() ).biasGradients;
        addEachGradientToResultVector( entry.getValue().biasGradients, resultVector );
    }

    private void addWeightGradientsToResult(Map<Integer, GradientsObject> resultGradients, Map.Entry<Integer, GradientsObject> entry) {
        Matrix resultMatrix = resultGradients.get( entry.getKey() ).weightGradients;
        addEachGradientToResultMatrix( entry.getValue().weightGradients, resultMatrix );
    }

    private void addEachGradientToResultVector(Vector biasGradients, Vector resultVector) {
        for (int i = 0; i < resultVector.size(); i++) {
            resultVector.set(i, resultVector.get( i ) + biasGradients.get( i ) );
        }
    }

    private void addEachGradientToResultMatrix( Matrix gradients, Matrix resultMatrix ) {
        for ( int i = 0; i < resultMatrix.getNumRows(); i++ ) {
            for ( int j = 0; j < resultMatrix.getNumCols(); j++ ) {
                resultMatrix.set( i, j, resultMatrix.get( i, j ) + gradients.get( i, j ) );
            }
        }
    }

    private Map<Integer, GradientsObject> createEmptyResultGradientMap( List<Map<Integer, GradientsObject>> gradientsList ) {
        Map<Integer, GradientsObject> resultGradients = new HashMap<>();
        for ( int i = 0; i < gradientsList.get( 0 ).size(); i++ ) {
            Matrix resultMatrix = new Matrix( gradientsList.get( 0 ).get( i ).weightGradients.getNumRows(), gradientsList.get( 0 ).get( i ).weightGradients.getNumCols() );
            Vector resultVector = new Vector(gradientsList.get( 0 ).get( i ).biasGradients.size() );
            resultGradients.put( i, new GradientsObject(resultMatrix, resultVector));
        }
        return resultGradients;
    }

    private Map<Integer, GradientsObject> calcGradients( Vector input, Vector expectedOutput, CostFunction costFunction ) {
        nn.validateInputVector( input );

        Map<Integer, Layer.TrainingResult> layerIndexToTrainingResult = feedForward( input );
        return backpropagate( expectedOutput, costFunction, layerIndexToTrainingResult, input );
    }

    private Map<Integer, GradientsObject> backpropagate( Vector expectedOutput, CostFunction costFunction, Map<Integer, Layer.TrainingResult> layerIndexToTrainingResult, Vector input ) {
        Map<Integer, GradientsObject> layerIndexToGradient = new HashMap<>();
        for ( int i = nn.layers.size() - 1; i >= 0; i-- ) {
            calculateDerivativesOfCostFunction( i, expectedOutput, costFunction, layerIndexToTrainingResult );
            Matrix weightGradients = weightGradientsForEachWeightOfLayer( layerIndexToTrainingResult, i, nn.layers.get( i ), input );
            Vector biasGradients = biasGradientsForEachBiasOfLayer( layerIndexToTrainingResult, i, nn.layers.get( i ));
            layerIndexToGradient.put( i, new GradientsObject(weightGradients, biasGradients));
        }
        return layerIndexToGradient;
    }

    private Vector biasGradientsForEachBiasOfLayer(Map<Integer, Layer.TrainingResult> layerIndexToTrainingResult, int i, Layer layer) {
        Vector gradients = new Vector( layer.getBias().size() );
        for (int j = 0; j < gradients.size(); j++) {
            double dervOfActivationFunction = getDerivativeOfActivationFunctionFromLayer(layer, j, layerIndexToTrainingResult.get(i));
            double dervOfCostFunction = layerIndexToTrainingResult.get( i ).getDervOfCostFunction().get( j );
            double value = dervOfActivationFunction * dervOfCostFunction;
            gradients.set(j, value);
        }
        return gradients;
    }

    private double getDerivativeOfActivationFunctionFromLayer(Layer layer, int neuronIndex, Layer.TrainingResult trainingResult) {
        return layer.getFActivation().derivative(trainingResult.getOutputStripped().get(neuronIndex));
    }

    private Matrix weightGradientsForEachWeightOfLayer( Map<Integer, Layer.TrainingResult> layerIndexToTrainingResult, int i, Layer layer, Vector input ) {
        Matrix gradients = new Matrix( layer.getWeights().getNumRows(), layer.getWeights().getNumCols() );
        for ( int j = 0; j < gradients.getNumRows(); j++ ) {
            for ( int k = 0; k < gradients.getNumCols(); k++ ) {
                double neuronKOfPrevLayer = i > 0 ? layerIndexToTrainingResult.get( i - 1 ).getOutputWithActivationFunction().get( k ) : input.get( k );
                double dervOfActivationFunction = getDerivativeOfActivationFunctionFromLayer(layer, j, layerIndexToTrainingResult.get( i ));
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
            double dervOfActivationFunctionWithOutputStripped = getDerivativeOfActivationFunctionFromLayer( followingLayer, i, trainingResultFollowingLayer );
            double dervOfCostFunctionOfNeuronI = trainingResultFollowingLayer.getDervOfCostFunction().get( i );
            temp = weightFromNeuronIndexToI * dervOfActivationFunctionWithOutputStripped * dervOfCostFunctionOfNeuronI;

        }
        return temp;
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

    private static class GradientsObject {
        final Matrix weightGradients;
        final Vector biasGradients;

        public GradientsObject(Matrix weightGradients, Vector biasGradients) {
            this.weightGradients = weightGradients;
            this.biasGradients = biasGradients;
        }

    }

}
