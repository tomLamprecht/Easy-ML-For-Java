package de.fhws.easyml.ai.convolutionalneuralnetwork.layer.convolutionlayer;

import de.fhws.easyml.linearalgebra.Matrix;
import de.fhws.easyml.linearalgebra.Randomizer;

public class Filter {
    private final Matrix feature;

    public Filter(Matrix feature) {
        this.feature = feature;
    }

    public static Filter createRandomizedFilter(int row, int col, Randomizer randomizer, int[] striding, boolean doPadding) {
        Matrix feature = new Matrix(row, col);
        feature.randomize(randomizer);
        return new Filter(feature);
    }


    public Matrix doFiltering(Matrix matrix) {
        return null;
    }
}
