package de.fhws.easyml.geneticalgorithm;

import de.fhws.easyml.utility.MultiThreadHelper;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public final class Population<T extends Individual<T>> implements Serializable {
    private List<T> individuals;

    private int generation;

    public Population() {
        this.individuals = new ArrayList<>();
    }

    public Population(List<T> individuals) {
        this.individuals = new ArrayList<>(individuals);
    }

    void calcFitnesses(ExecutorService executor) {
        if(executor != null) {
            MultiThreadHelper.callConsumerOnCollection(executor, individuals, T::calcFitness);
        }
        else {
            individuals.forEach(T::calcFitness);
        }
    }

    /**
     * sorts the population in DESCENDING order.
     * It uses the compareTo method of the solutions for this.
     */
    public void sortPopByFitness() {
        individuals.sort(Comparator.reverseOrder());
    }

    public T getBest() {
        return Collections.max(individuals);
    }

    public double getAverageFitness() {
        return individuals.stream().mapToDouble(Individual::getFitness).average().getAsDouble();
    }

    public int getGeneration() {
        return generation;
    }

    public void incGeneration() {
        generation++;
    }

    public int getSize() {
        return individuals.size();
    }

    /**
     * Replaces all Individuals with a <strong>copy</strong> of the given Individuals <strong>if and only if</strong> the reference of this Individual occurs more than once in the given collection.
     * Otherwise, the Individuals get replaced with the Individuals in {@code collection}
     *
     * @param collection of Individuals which may be copied
     */
    public void replaceAllIndividuals(Collection<Individual<T>> collection) {
        individuals.clear();

        Map<Integer, Long> referenceMap = collection.stream().collect( Collectors.groupingBy( System::identityHashCode, Collectors.counting() ) );

        individuals.addAll( collection.stream()
                .map( individual -> doesElementOccurMoreThanOnce( referenceMap, individual ) ? individual.copy() : individual.getThis() )
                .collect( Collectors.toList() ) );
    }

    private boolean doesElementOccurMoreThanOnce( Map<Integer, Long> referenceMap, Individual<T> element ) {
        return referenceMap.merge( System.identityHashCode( element ), -1L, Long::sum ) > 0;
    }

    public List<T> getIndividuals() {
        return individuals;
    }

    /**
     * Replaces all Individuals with the given ones. Be aware that when 2 or more Individuals refer to the same reference there might be unwanted sight effects
     * in the mutation process. It's therefore recommended using the {@link #replaceAllIndividuals(Collection)} method if one is not sure whether 2 or more Individuals share
     * the same reference
     * @param individuals that replace the old ones
     * @throws NullPointerException if given individuals are null
     */
    public void setIndividuals(List<T> individuals) {
        if (individuals == null)
            throw new NullPointerException("Individuals can't be null");
        this.individuals = individuals;
    }

}
