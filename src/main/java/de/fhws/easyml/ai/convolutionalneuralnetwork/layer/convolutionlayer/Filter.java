package de.fhws.easyml.ai.convolutionalneuralnetwork.layer.convolutionlayer;

import de.fhws.easyml.linearalgebra.Matrix;
import de.fhws.easyml.linearalgebra.Randomizer;

public class Filter {
    private final Matrix feature;
    private final int rowStriding;
    private final int colStriding;
    private final double bias;
    private final boolean padding;


    public Filter( Matrix feature, double bias, int rowStriding, int colStriding, boolean padding ) {
        this.feature = feature;
        this.bias = bias;
        this.rowStriding = rowStriding;
        this.colStriding = colStriding;
        this.padding = padding;
    }

    public static Filter createRandomizedFilter( int row, int col, Randomizer featureRadomizer, Randomizer biasRandomizer, int rowStriding, int colStriding, boolean doPadding ) {
        Matrix feature = new Matrix( row, col );
        feature.randomize( featureRadomizer );
        return new Filter( feature, biasRandomizer.getInRange(), rowStriding, colStriding, doPadding );
    }

    protected int calculateResultDataRows( int orgRows ) {
        int rowStart = resultDataStartRowIndex();
        int rowEnd = resultDataEndRowIndex( orgRows );
        return calculateResultDataLengthByIndex( rowStart, rowEnd, rowStriding );
    }

    protected int calculateResultDataCols( int orgCols ) {
        int colStart = resultDataStartColIndex();
        int colEnd = resultDataEndColIndex( orgCols );
        return calculateResultDataLengthByIndex( colStart, colEnd, colStriding );
    }

    private int calculateResultDataLengthByIndex( int start, int end, int striding ) {
        return (int) Math.ceil( ( end - start ) * 1d / striding );
    }

    private int resultDataStartRowIndex() {
        return padding ? -( feature.getNumRows() - 1 ) : 0;
    }

    private int resultDataEndRowIndex( int orgRows ) {
        return padding ? orgRows : orgRows - feature.getNumRows() + 1;
    }

    private int resultDataStartColIndex() {
        return padding ? -( feature.getNumCols() - 1 ) : 0;
    }

    private int resultDataEndColIndex( int orgCols ) {
        return padding ? orgCols : orgCols - feature.getNumCols() + 1;
    }

    public Matrix doFiltering( Matrix matrix ) {
        int rowStart = resultDataStartRowIndex();
        int rowEnd = resultDataEndRowIndex( matrix.getNumRows() );
        int colStart = resultDataStartColIndex();
        int colEnd = resultDataEndColIndex( matrix.getNumCols() );

        return new Matrix( doFeatureExtraction( matrix, rowStart, rowEnd, colStart, colEnd ) );
    }

    private double[][] createEmptyResultData( int rowStart, int rowEnd, int colStart, int colEnd ) {
        int rowLen = calculateResultDataLengthByIndex( rowStart, rowEnd, rowStriding );
        int colLen = calculateResultDataLengthByIndex( colStart, colEnd, colStriding );
        return new double[rowLen][colLen];
    }

    private double[][] doFeatureExtraction( Matrix matrix, int rowStart, int rowEnd, int colStart, int colEnd ) {
        double[][] resultData = createEmptyResultData( rowStart, rowEnd, colStart, colEnd );

        for ( int row = rowStart; row < rowEnd; row += rowStriding ) {
            for ( int col = colStart; col < colEnd; col += colStriding ) {
                resultData[( row - rowStart ) / rowStriding][( col - colStart ) / colStriding] = extractFeature( matrix.getSubMatrix( row, col, feature.getNumRows(), feature.getNumCols() ) ) + bias;
            }
        }
        return resultData;
    }

    private double extractFeature( Matrix src ) {
        double result = 0;
        for ( int row = 0; row < src.getNumRows(); row++ ) {
            for ( int col = 0; col < src.getNumCols(); col++ ) {
                result += src.get( row, col ) * feature.get( row, col );
            }
        }
        return result;
    }

    public Matrix getFeature() {
        return feature;
    }
}
