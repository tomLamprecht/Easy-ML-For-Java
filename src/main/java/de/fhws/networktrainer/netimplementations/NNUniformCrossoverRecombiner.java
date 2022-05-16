package de.fhws.networktrainer.netimplementations;

import de.fhws.geneticalgorithm.Population;
import de.fhws.networks.neuralnetworks.Layer;
import de.fhws.networktrainer.NetworkRecombiner;
import de.fhws.networktrainer.NeuralNetIndividual;
import de.fhws.utility.ListUtility;
import de.fhws.utility.Validator;

import java.util.List;
import java.util.Random;


public class NNUniformCrossoverRecombiner implements NetworkRecombiner<NeuralNetIndividual> {

    private int amountOfParentsPerChild;

    public NNUniformCrossoverRecombiner(int amountOfParentsPerChild) {
        this.amountOfParentsPerChild = amountOfParentsPerChild;
    }

    @Override
    public void recombine(Population<NeuralNetIndividual> pop, int goalSize) {
        Validator.validateBetweenAndThrow(amountOfParentsPerChild, 1, pop.getSize());

        while (pop.getIndividuals().size() < goalSize) {
            List<NeuralNetIndividual> parents = ListUtility.selectRandomElements(pop.getIndividuals(), amountOfParentsPerChild);
            pop.getIndividuals().add(makeChild(parents));
        }
    }



    private NeuralNetIndividual makeChild(List<NeuralNetIndividual> parents) {
        NeuralNetIndividual child = parents.get(0).copy();
        for(int l = 0; l < child.getNN().getLayers().size(); l++) {
            combineWeights(parents, child, l);

            combineBias(parents, child, l);
        }
        return child;
    }

    private static void combineWeights(List<NeuralNetIndividual> parents, NeuralNetIndividual child, int layerIndex) {
        Layer layer = child.getNN().getLayers().get(layerIndex);

        for (int i = 0; i < layer.getWeights().getNumRows(); i++) {
            for (int j = 0; j < layer.getWeights().getNumCols(); j++) {
                int selectedParent = new Random().nextInt(parents.size());
                layer.getWeights().set(i, j, parents.get(selectedParent).getNN().getLayers().get(layerIndex).getWeights().get(i,j));
            }
        }
    }

    private static void combineBias(List<NeuralNetIndividual> parents, NeuralNetIndividual child, int layerIndex) {
        Layer layer = child.getNN().getLayers().get(layerIndex);

        for (int i = 0; i < layer.getBias().size(); i++) {
            int selectedParent = new Random().nextInt(parents.size());
            layer.getBias().set(i, parents.get(selectedParent).getNN().getLayers().get(layerIndex).getBias().get(i));
        }
    }

}
