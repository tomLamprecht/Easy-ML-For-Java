package de.fhws.easyml.ai.convolutionalneuralnetwork.layer.poolinglayer;

import de.fhws.easyml.linearalgebra.Matrix;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PoolingLayer {
    int rowSize, colSize;
    Function<Matrix, Double> poolingFunction;

    public PoolingLayer( int rowSize, int colSize, Function<Matrix, Double> poolingFunction ) {
        this.rowSize = rowSize;
        this.colSize = colSize;
        this.poolingFunction = poolingFunction;
    }

    public PoolingLayer( int size, Function<Matrix, Double> poolingFunction ) {
        this.rowSize = size;
        this.colSize = size;
        this.poolingFunction = poolingFunction;
    }

    /**
     * calculate the pooled Matrix for the input Matrix. If the size of the input is not divisible
     * by the pooling size there will be a padding technique applied.
     * This is used in Convolutional Neural Networks to decrease the complexity of the Network.
     *
     * @param input Matrix that will be pooled
     * @return pooled Matrix
     */
    public Matrix pool( Matrix input ) {
        double[][] pooledData = createEmptyPooling2dArray( input );

        insertPooledValuesInto2dArray( input, pooledData );

        return new Matrix( pooledData );
    }

    /**
     * calculates the pooled Matrix for all matrices in input
     *
     * @param input list of matrices
     * @return pooled matrices
     * @see {{@link #pool(Matrix)}}
     */
    public List<Matrix> pool( List<Matrix> input ) {
        return input.stream().map( this::pool ).collect( Collectors.toList() );
    }

    private void insertPooledValuesInto2dArray( Matrix input, double[][] result ) {
        for ( int row = 0; row < input.getNumRows() - rowSize + 1; row += rowSize ) {
            for ( int col = 0; col < input.getNumCols() - colSize + 1; col += colSize ) {
                double pooledValue = poolingFunction.apply( input.getSubMatrix( row, col, rowSize, colSize ) );
                result[row / rowSize][col / colSize] = pooledValue;
            }
        }
    }

    private double[][] createEmptyPooling2dArray( Matrix input ) {
        int resultRows = input.getNumRows() / rowSize;
        int resultCols = input.getNumCols() / colSize;
        return new double[resultRows][resultCols];
    }

}
