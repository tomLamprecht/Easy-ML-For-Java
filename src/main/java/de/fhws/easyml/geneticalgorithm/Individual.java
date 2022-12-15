package de.fhws.easyml.geneticalgorithm;

import java.io.Serializable;

public interface Individual<T extends Individual<T>> extends Comparable<Individual<T>>, Serializable{
	
     void calcFitness();
     
     double getFitness();
     
     T copy();

     //This Warning can be suppressed, because the only way this could crash is
     //when a class is implementing Individual but gives another class but itself as the Generic Type
    //This is not an expected behavior and the whole program would crash anyway, therefore there is no further need to handle this cast
    @SuppressWarnings("unchecked")
     default T getThis(){
         try {
             return (T) this;
         }
         catch (ClassCastException e) {
             throw new RuntimeException("Couldn't get an instance of individual. Maybe your implementation of Individual gives another Generic Type but itself to the Individual.");
         }
     }

     default int compareTo(Individual o) {
 		if (getFitness() - o.getFitness() == 0)
			return 0;
		else
			return getFitness() - o.getFitness() < 0 ? -1 : 1;
     }

}
