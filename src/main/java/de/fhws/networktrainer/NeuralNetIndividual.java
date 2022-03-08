package de.fhws.networktrainer;

import de.fhws.linearalgebra.Vector;
import de.fhws.networks.neuralnetworks.NeuralNet;

public class NeuralNetIndividual implements NetworkIndividual<Vector, NeuralNetIndividual>  {

    private NeuralNet neuralNet;
    private NeuralNetFitnessFunction fitnessFunction;
    private double fitness;

    public NeuralNetIndividual(NeuralNet neuralNet, NeuralNetFitnessFunction fitnessFunction) {
        this.neuralNet = neuralNet;
        this.fitnessFunction = fitnessFunction;
    }


    public NeuralNet getNN() {
        return neuralNet;
    }

    @Override
    public void calcFitness() {
        fitness = fitnessFunction.applyAsDouble(neuralNet);
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    @Override
    public NeuralNetIndividual copy() {
        return new NeuralNetIndividual(neuralNet.copy(), fitnessFunction);
    }

    @Override
    public Vector calcOutput(Vector vector) {
        return neuralNet.calcOutput(vector);
    }
}
