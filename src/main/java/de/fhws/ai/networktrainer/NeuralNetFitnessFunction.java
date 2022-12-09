package de.fhws.ai.networktrainer;

import de.fhws.ai.neuralnetwork.NeuralNet;

@FunctionalInterface
public interface NeuralNetFitnessFunction {

    double calculateFitness( NeuralNet neuralNet );

}
