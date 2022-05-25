package de.fhws.geneticalgorithm;

import de.fhws.utility.MultiThreadHelper;

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

    void calcFitnesses(Optional<ExecutorService> executor) {
        executor.ifPresentOrElse(exec -> MultiThreadHelper.callConsumerOnCollection(exec, individuals, T::calcFitness)
                , () -> individuals.forEach(Individual::calcFitness));
    }

    /**
     * sorts the population in DESCENDING order.
     * It uses the compareTo method of the solutions for this.
     */
    public void sortPopByFitness() {
        Collections.sort(individuals, Comparator.reverseOrder());
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
     * Replaces all Individuals with a copy of the Individuals if the reference of this Individual occurs more than once in the given collection
     *
     * @param collection of Individuals which will be copied
     */
    public void replaceAllIndividuals(Collection<Individual<T>> collection) {
        individuals.clear();

        Map<Integer, Long> referenceMap = collection.stream().collect( Collectors.groupingBy( System::identityHashCode, Collectors.counting() ) );

        individuals.addAll( collection.stream()
                .map( element -> referenceMap.merge( System.identityHashCode( element ), -1L, Long::sum ) > 0 ? element.copy() : element.getThis() )
                .collect( Collectors.toList() ) );
    }

    public List<T> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<T> individuals) {
        if (individuals == null)
            throw new NullPointerException("Individuals can't be null");
        this.individuals = individuals;
    }

}
