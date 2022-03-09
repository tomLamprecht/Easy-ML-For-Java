package de.fhws.networktrainer;

import java.util.List;

import de.fhws.linearalgebra.Matrix;
import de.fhws.linearalgebra.Vector;
import de.fhws.networks.convulotionalnn.ConvolutionalNet;

public class ConvolutionalNNIndividual implements NetworkIndividual<List<Matrix>, ConvolutionalNNIndividual> {

	private ConvolutionalNet cnn;
    private CNNFitnessFunction fitnessFunction;
	private double fitness;
	
	public ConvolutionalNNIndividual(ConvolutionalNet cnn, CNNFitnessFunction fitnessFunction) {
		this.cnn = cnn;
		this.fitnessFunction = fitnessFunction;
	}
	
	
	public ConvolutionalNet get() {
		return cnn;
	}
	
	@Override
	public Vector calcOutput(List<Matrix> t) {
		return cnn.calcOutput(t);
	}

	@Override
	public void calcFitness() {
		fitness = fitnessFunction.applyAsDouble(cnn);
	}

	@Override
	public double getFitness() {
		return fitness;
	}

	@Override
	public ConvolutionalNNIndividual copy() {
		return new ConvolutionalNNIndividual(cnn.copy(), fitnessFunction);
	}
	
	
	
}
