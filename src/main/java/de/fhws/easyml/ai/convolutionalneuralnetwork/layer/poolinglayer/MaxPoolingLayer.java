package de.fhws.easyml.ai.convolutionalneuralnetwork.layer.poolinglayer;

import de.fhws.easyml.linearalgebra.Matrix;

public class MaxPoolingLayer extends PoolingLayer {
    public MaxPoolingLayer(int rowSize, int colSize) {
        super(rowSize, colSize, Matrix::getHighestNumber);
    }

    public MaxPoolingLayer(int size) {
        this(size, size);
    }
}
