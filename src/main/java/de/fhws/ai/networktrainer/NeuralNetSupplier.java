package de.fhws.ai.networktrainer;

import de.fhws.ai.neuralnetwork.NeuralNet;

import java.util.function.Supplier;

@FunctionalInterface
public interface NeuralNetSupplier extends Supplier<NeuralNet> {

}
