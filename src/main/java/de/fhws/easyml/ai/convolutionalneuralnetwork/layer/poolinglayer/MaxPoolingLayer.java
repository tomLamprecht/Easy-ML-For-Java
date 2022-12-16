package de.fhws.easyml.ai.convolutionalneuralnetwork.layer.poolinglayer;

public class MaxPoolingLayer extends PoolingLayer {
    public MaxPoolingLayer(int rowSize, int colSize) {
        super(rowSize, colSize, m -> m.getDataStream().max().orElseThrow());
    }

    public MaxPoolingLayer(int size) {
        this(size, size);
    }
}
