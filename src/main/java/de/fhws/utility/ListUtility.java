package de.fhws.utility;

import de.fhws.networktrainer.NeuralNetIndividual;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListUtility {
    public static <T> List<T> selectRandomElements(List<T> list, int amount) {
        List<T> selectedElements = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            int index = new Random().nextInt(list.size());
            T element = list.get(index);
            if (!selectedElements.contains(element))
                selectedElements.add(element);
            else
                i--;
        }
        return selectedElements;
    }
}
