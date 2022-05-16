package de.fhws.geneticalgorithm;

import java.io.File;

import de.fhws.utility.FileHandler;

class IntervalSaver {

	private int counter;
	private final int intervall;
	private final boolean override;
	private final File dir;
	
	public IntervalSaver(int intervall, boolean override, File dir) {
		this.intervall = intervall;
		this.override = override;
		this.dir = dir;
	}

	public void save(Population<? extends Individual> pop) {
		
		if(intervall % counter != 0)
			FileHandler.writeObjectToAGeneratedFileLocation(pop, "population", dir.getAbsolutePath(), !override, ".ser", true);
		counter++;
	}
	
	
}
