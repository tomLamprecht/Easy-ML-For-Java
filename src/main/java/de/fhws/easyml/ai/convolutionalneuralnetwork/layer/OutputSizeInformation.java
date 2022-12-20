package de.fhws.easyml.ai.convolutionalneuralnetwork.layer;

public class OutputSizeInformation {
    private final int rows;
    private final int cols;
    private final int channels;

    public OutputSizeInformation( int rows, int cols, int channels ) {
        this.rows = rows;
        this.cols = cols;
        this.channels = channels;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getChannels() {
        return channels;
    }
}
