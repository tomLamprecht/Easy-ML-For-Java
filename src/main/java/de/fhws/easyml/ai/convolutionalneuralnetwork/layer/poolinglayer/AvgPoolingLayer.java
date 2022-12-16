package de.fhws.easyml.ai.convolutionalneuralnetwork.layer.poolinglayer;

public class AvgPoolingLayer extends PoolingLayer {
    public AvgPoolingLayer(int rowSize, int colSize) {
        super(rowSize, colSize, m -> m.getDataStream().average().orElseThrow());
    }

    public AvgPoolingLayer(int size) {
        this(size,size);
    }
}
