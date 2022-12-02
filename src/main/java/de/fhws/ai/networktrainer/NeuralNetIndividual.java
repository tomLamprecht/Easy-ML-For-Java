package de.fhws.ai.networktrainer;

import de.fhws.ai.linearalgebra.Vector;
import de.fhws.ai.neuralnetwork.NeuralNet;


public class NeuralNetIndividual implements de.fhws.ai.geneticalgorithm.Individual<NeuralNetIndividual> {

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
        fitness = fitnessFunction.calculateFitness(neuralNet);
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    /**
     * Copies the Neural Net but uses the same reference for the fitnessFunction
     * @return the copy
     */
    @Override
    public NeuralNetIndividual copy() {
        NeuralNetIndividual copy = new NeuralNetIndividual(neuralNet.copy(), fitnessFunction);
        copy.fitness = this.fitness;
        return copy;
    }

    public Vector calcOutput(Vector vector) {
        return neuralNet.calcOutput(vector);
    }


    public NeuralNetFitnessFunction getFitnessFunction() {
        return fitnessFunction;
    }
}
