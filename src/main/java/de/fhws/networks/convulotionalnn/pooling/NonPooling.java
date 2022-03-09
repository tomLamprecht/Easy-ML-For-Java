package de.fhws.networks.convulotionalnn.pooling;

import de.fhws.linearalgebra.Matrix;

public class NonPooling extends Pooling{

	/**
	 * Creates a Pooling Layer that does nothing
	 * 
	 */
	public NonPooling() {
		
	}
	
	@Override
	public Matrix pool(Matrix level) {
		return level;
	}

}
