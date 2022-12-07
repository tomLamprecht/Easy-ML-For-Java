package de.fhws.ai.geneticalgorithm.logger.graphplotter.lines;

import de.fhws.ai.geneticalgorithm.Individual;
import de.fhws.ai.geneticalgorithm.Population;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class LineGenerator{
    private final String name;
    private final List<Double> values = new ArrayList<>();

    public LineGenerator(String name) {
        this.name = name;
    }

    public List<Double> getValues() {
        return values;
    }

    public Double getValue(int index){
        return values.get(index);
    }

    public String getName() {
        return name;
    }

    public void log(Population<?> pop){
        values.add(convert(pop));
    }

    private double convert(Population<?> pop){
        return getConverter().apply(pop);
    }


    /**
     * This function converts a Population into a single Double value that can be plotted onto a graph
     * @return the converted value
     */
    protected abstract Function<Population<?>, Double> getConverter();
}