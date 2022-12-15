package de.fhws.easyml.ai.geneticneuralnet;

import de.fhws.easyml.ai.neuralnetwork.NeuralNet;

import java.util.function.Supplier;

@FunctionalInterface
public interface NeuralNetSupplier extends Supplier<NeuralNet> {

}
