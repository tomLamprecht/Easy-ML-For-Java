package de.fhws.easyml.geneticalgorithm.saver;

import de.fhws.easyml.geneticalgorithm.Individual;
import de.fhws.easyml.geneticalgorithm.Population;
import de.fhws.easyml.utility.FileHandler;

import java.io.File;

public class IntervalSaver {

	private int counter;
	private final int intervall;
	private final boolean override;
	private final File dir;
	
	public IntervalSaver(int intervall, boolean override, File dir) {
		this.intervall = intervall;
		this.override = override;
		this.dir = dir;
	}

	public void save(Population<? extends Individual<?>> pop) {
		
		if(counter % intervall == 0)
			FileHandler.writeObjectToAGeneratedFileLocation(pop, "population", dir.getAbsolutePath(), !override, ".ser", true);

		counter++;
	}
	
	
}
