package de.fhws.easyml.ai.geneticneuralnet;

import de.fhws.easyml.ai.neuralnetwork.NeuralNet;

@FunctionalInterface
public interface NeuralNetFitnessFunction {

    double calculateFitness( NeuralNet neuralNet );

}
