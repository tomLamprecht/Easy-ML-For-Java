package de.fhws.easyml.geneticalgorithm.populationsupplier;

import de.fhws.easyml.geneticalgorithm.Population;
import de.fhws.easyml.geneticalgorithm.Individual;
import de.fhws.easyml.utility.FileHandler;

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
