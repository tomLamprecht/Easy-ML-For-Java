package de.fhws.easyml.ai.convolutionalneuralnetwork.layer.convolutionlayer;

import de.fhws.easyml.linearalgebra.Matrix;
import de.fhws.easyml.linearalgebra.Randomizer;

public class Filter {
    private final Matrix feature;
    private final int rowStriding;
    private final int colStriding;
    private final boolean padding;

    public Filter(Matrix feature, int rowStriding, int colStriding, boolean padding) {
        this.feature = feature;
        this.rowStriding = rowStriding;
        this.colStriding = colStriding;
        this.padding = padding;
    }

    public static Filter createRandomizedFilter(int row, int col, Randomizer randomizer, int rowStriding, int colStriding, boolean doPadding) {
        Matrix feature = new Matrix(row, col);
        feature.randomize(randomizer);
        return new Filter(feature, rowStriding, colStriding, doPadding);
    }


    public Matrix doFiltering(Matrix matrix) {
        int rowStart = padding ? -(feature.getNumRows()-1) : 0;
        int rowEnd = padding ? matrix.getNumRows() : matrix.getNumRows() - feature.getNumRows() + 1;
        int colStart = padding ? -(feature.getNumCols()-1) : 0;
        int colEnd = padding ? matrix.getNumCols() : matrix.getNumCols() - feature.getNumCols() + 1;

        double[][] resultData = new double[(int) Math.ceil((rowEnd-rowStart)*1d / rowStriding)][(int)Math.ceil((colEnd-colStart) *1d/ colStriding)];

        doFeatureExtraction(matrix, rowStart, rowEnd, colStart, colEnd, resultData);

        return new Matrix(resultData);
    }

    private void doFeatureExtraction(Matrix matrix, int rowStart, int rowEnd, int colStart, int colEnd, double[][] resultData) {
        for (int row = rowStart; row < rowEnd; row+=rowStriding) {
            for (int col = colStart; col < colEnd; col+=colStriding) {
                resultData[(row-rowStart) / rowStriding][(col-colStart) / colStriding] =extractFeature(matrix.getSubMatrix(row,col, feature.getNumRows(), feature.getNumCols()));
            }
        }
    }

    private double extractFeature(Matrix src){
        double result = 0;
        for (int row = 0; row < src.getNumRows(); row++) {
            for (int col = 0; col < src.getNumCols(); col++) {
                result += src.get(row,col) * feature.get(row, col);
            }
        }
        return result;
    }
}
