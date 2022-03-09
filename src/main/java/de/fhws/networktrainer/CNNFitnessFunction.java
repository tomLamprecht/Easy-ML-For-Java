package de.fhws.networktrainer;

import java.util.function.ToDoubleFunction;

import de.fhws.networks.convulotionalnn.ConvolutionalNet;

public interface CNNFitnessFunction extends ToDoubleFunction<ConvolutionalNet>{

}
