package de.fhws.networktrainer.netimplementations;

import de.fhws.geneticalgorithm.Population;
import de.fhws.networks.neuralnetworks.NeuralNet;
import de.fhws.networktrainer.NetworkMutator;
import de.fhws.networktrainer.NeuralNetIndividual;
import de.fhws.utility.Validator;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.DoubleUnaryOperator;

public class NNRandomMutator implements NetworkMutator<NeuralNetIndividual> {

    private final double outerMutationRate;
    private double innerMutationRate;
    private double mutationFactor;

    private final DoubleUnaryOperator innerMutator = d -> Math.random() < innerMutationRate ? d * mutationFactor : d;

    public NNRandomMutator(double outerMutationRate, double innerMutationRate, double mutationFactor) {
        Validator.validateBetweenAndThrow(outerMutationRate, 0, 1);
        Validator.validateBetweenAndThrow(innerMutationRate, 0, 1);

        this.outerMutationRate = outerMutationRate;
        this.innerMutationRate = innerMutationRate;
        this.mutationFactor = mutationFactor;
    }

    @Override
    public void mutate(Population<NeuralNetIndividual> pop, Optional<ExecutorService> executorService) {
        pop.getIndividuals()
                .stream()
                .filter(individual -> Math.random() < outerMutationRate)
                .forEach(individual -> mutateNN(individual.getNN()));
    }

    private void mutateNN(NeuralNet neuralNet) {
        neuralNet.getLayers()
                .forEach(layer -> {
                    layer.getWeights().apply(innerMutator);
                    layer.getBias().apply(innerMutator);
                });
    }

}
