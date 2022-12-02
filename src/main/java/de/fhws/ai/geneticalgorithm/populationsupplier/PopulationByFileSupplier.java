package de.fhws.ai.geneticalgorithm.populationsupplier;

import de.fhws.ai.geneticalgorithm.Individual;
import de.fhws.ai.geneticalgorithm.Population;
import de.fhws.ai.utility.FileHandler;

import java.io.File;

public class PopulationByFileSupplier<T extends Individual<T>> implements PopulationSupplier<T> {

	private final File file;

	public PopulationByFileSupplier( File file ) {
		this.file = file;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Population<T> get() {
		Object posPop = FileHandler.getFirstObjectFromFile( file );
		if(posPop instanceof Population<?>)
			return (Population<T>) posPop;
		throw new IllegalArgumentException("file: \"" + file + "\" does not contain a Population of T as first Object.");
	}
}
