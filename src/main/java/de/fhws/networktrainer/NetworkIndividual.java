package de.fhws.networktrainer;

import de.fhws.geneticalgorithm.Individual;
import de.fhws.networks.Network;

public interface NetworkIndividual<T, C extends NetworkIndividual<T, C>> extends Network<T>, Individual<C> {
}
