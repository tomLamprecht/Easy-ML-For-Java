package de.fhws.networks.convulotionalnn.pooling;

import java.io.Serializable;

import de.fhws.linearalgebra.Matrix;

public abstract class Pooling implements Serializable {

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
