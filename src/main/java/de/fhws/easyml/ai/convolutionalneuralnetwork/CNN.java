package de.fhws.easyml.ai.convolutionalneuralnetwork;


import de.fhws.easyml.ai.convolutionalneuralnetwork.layer.Layer;
import de.fhws.easyml.ai.convolutionalneuralnetwork.layer.OutputSizeInformation;
import de.fhws.easyml.ai.convolutionalneuralnetwork.layer.convolutionlayer.ConvolutionLayer;
import de.fhws.easyml.ai.convolutionalneuralnetwork.layer.poolinglayer.PoolingLayer;
import de.fhws.easyml.ai.neuralnetwork.NeuralNet;
import de.fhws.easyml.linearalgebra.Matrix;
import de.fhws.easyml.linearalgebra.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/*
  MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
        .layer(0, new ConvolutionLayer.Builder()
            .name("cnn1")
            .nIn(nChannels)
            .stride(1, 1)
            .nOut(20)
            .activation(Activation.IDENTITY)
            .build())
        .layer(1, new SubsamplingLayer.Builder()
            .name("maxpool1")
            .poolingType(SubsamplingLayer.PoolingType.MAX)
            .kernelSize(2, 2)
            .stride(2, 2)
            .build())
        .layer(2, new ConvolutionLayer.Builder()
            .name("cnn2")
            .stride(1, 1)
            .nOut(50)
            .activation(Activation.IDENTITY)
            .build())
        .layer(3, new SubsamplingLayer.Builder()
            .name("maxpool2")
            .poolingType(SubsamplingLayer.PoolingType

 */
public class CNN {

    List<Layer> layers;
    NeuralNet nn;

    private CNN( List<Layer> layers, NeuralNet nn ) {
        this.layers = layers;
        this.nn = nn;
    }

    public Vector calculateResult( List<Matrix> prev ) {
        for ( Layer layer : layers )
            prev = layer.process( prev );
        return nn.calcOutput( parseListOfMatricesToVector( prev ) );
    }

    public Vector parseListOfMatricesToVector( List<Matrix> matrices ) {
        return new Vector( matrices.stream().flatMapToDouble( Matrix::getDataStream ).toArray() );
    }

    public static class Builder {

        List<Layer> layers = new ArrayList<>();

        private final int width, height, inputChannels;
        private final int outputSize;
        private final Function<NeuralNet.Builder, NeuralNet> builderFinisher;


        public Builder( int width, int height, int inputChannels, int outputSize, Function<NeuralNet.Builder, NeuralNet> builderFinisher ) {
            this.width = width;
            this.height = height;
            this.inputChannels = inputChannels;
            this.outputSize = outputSize;
            this.builderFinisher = builderFinisher;
        }

        private static int calculateNNInputSize( int width, int height, int inputChannels, List<Layer> layers ) {
            OutputSizeInformation prev = new OutputSizeInformation( width, height, inputChannels );
            for ( Layer layer : layers )
                prev = layer.outputSizeInformation( prev );

            return prev.getRows() * prev.getCols() * prev.getChannels();
        }

        public Builder withConvolutionalLayer( ConvolutionLayer cl ) {
            layers.add( cl );
            return this;
        }

        public Builder withPoolingLayer( PoolingLayer pl ) {
            layers.add( pl );
            return this;
        }

        public CNN build() {
            int nnInputSize = calculateNNInputSize( width, height, inputChannels, layers );
            NeuralNet.Builder nnBuilder = new NeuralNet.Builder(nnInputSize , outputSize );
            return new CNN( layers, builderFinisher.apply( nnBuilder ));
        }


    }


}
