package de.fhws.geneticalgorithm;

import java.io.Serializable;

public interface Solution extends Comparable<Solution>, Serializable{
	
     void calcFitness();
     
     public double getFitness();
     
     public Solution copy();
     
     public default int compareTo(Solution o) {
 		if (getFitness() - o.getFitness() == 0)
			return 0;
		else
			return getFitness() - o.getFitness() < 0 ? -1 : 1;
     }
}
