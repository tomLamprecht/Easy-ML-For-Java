package de.fhws.ai.networktrainer;

import de.fhws.ai.neuralnetwork.NeuralNet;

import java.util.function.ToDoubleFunction;

@FunctionalInterface
public interface NeuralNetFitnessFunction {

    double calculateFitness( NeuralNet neuralNet );

}
