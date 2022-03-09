package de.fhws.networks.convulotionalnn.pooling;

import de.fhws.linearalgebra.Matrix;

public abstract class Pooling {

	protected int size;
	
	public abstract Matrix pool(Matrix level);

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public abstract Pooling copy();
	
	
}
