package de.fhws.geneticalgorithm;

import java.io.Serializable;

public interface Individual<T extends Individual<T>> extends Comparable<Individual<T>>, Serializable{
	
     void calcFitness();
     
     double getFitness();
     
     T copy();
     
     default int compareTo(Individual o) {
 		if (getFitness() - o.getFitness() == 0)
			return 0;
		else
			return getFitness() - o.getFitness() < 0 ? -1 : 1;
     }

}
