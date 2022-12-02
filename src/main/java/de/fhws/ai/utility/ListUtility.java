package de.fhws.ai.utility;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListUtility {
    public static <T> List<T> selectRandomElements(List<T> list, int amount) {
        if( amount > list.size())
            throw new IllegalArgumentException("amount can't be bigger than list size");

       return Stream.generate( () -> list.get( (int) ( ThreadLocalRandom.current().nextDouble() * list.size() ) ) )
            .distinct()
            .limit( amount )
            .collect( Collectors.toList() );
    }
}
