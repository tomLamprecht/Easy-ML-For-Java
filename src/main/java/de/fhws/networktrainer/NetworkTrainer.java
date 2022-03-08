package de.fhws.networktrainer;

import de.fhws.geneticalgorithm.GeneticAlgorithm;

public class NetworkTrainer<T extends NetworkIndividual<?, T>> {

    private final GeneticAlgorithm<T> genAlg;

    public NetworkTrainer(GeneticAlgorithm<T> genAlg) {
        this.genAlg = genAlg;
    }

    public T train() {
        return genAlg.solve();
    }


}
