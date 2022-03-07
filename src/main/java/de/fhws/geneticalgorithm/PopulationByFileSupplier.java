package de.fhws.geneticalgorithm;

import java.io.File;

import de.fhws.filehandler.FileHandler;

public class PopulationByFileSupplier<T extends Individual<T>> implements PopulationSupplier<T>{

	private File fname;
	
	@SuppressWarnings("unchecked")
	@Override
	public Population<T> get() {
		Object posPop = FileHandler.getFirstObjectFromFile(fname);
		if(posPop instanceof Population<?>)
			return (Population<T>) posPop;
		throw new IllegalArgumentException("file: \"" + fname + "\" does not contain a Population as first Object.");
	}
}
