package de.fhws.geneticalgorithm;

import java.io.Serializable;

public interface Individual extends Comparable<Individual>, Serializable{
	
     void calcFitness();
     
     double getFitness();
     
     Individual copy();
     
     default int compareTo(Individual o) {
 		if (getFitness() - o.getFitness() == 0)
			return 0;
		else
			return getFitness() - o.getFitness() < 0 ? -1 : 1;
     }
}
