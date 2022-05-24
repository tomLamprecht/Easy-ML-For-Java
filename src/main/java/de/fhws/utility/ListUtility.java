package de.fhws.utility;

import de.fhws.networktrainer.NeuralNetIndividual;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListUtility {
    public static <T> List<T> selectRandomElements(List<T> list, int amount) {
        if( amount > list.size())
            throw new IllegalArgumentException("amount can't be bigger than list size");

       return Stream.generate( () -> list.get( (int) ( Math.random() * list.size() ) ) )
            .distinct()
            .limit( amount )
            .collect( Collectors.toList() );
    }
}
