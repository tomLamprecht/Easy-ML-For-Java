package de.fhws.networktrainer;

import de.fhws.networks.neuralnetworks.NeuralNet;

import java.util.function.ToDoubleFunction;

public interface NeuralNetFitnessFunction extends ToDoubleFunction<NeuralNet> {
}
