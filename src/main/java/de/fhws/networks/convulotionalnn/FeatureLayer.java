package de.fhws.networks.convulotionalnn;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import de.fhws.linearalgebra.Matrix;
import de.fhws.networks.convulotionalnn.convolution.Convolution;
import de.fhws.networks.convulotionalnn.pooling.Pooling;

public class FeatureLayer {

	private Convolution con;
	private Pooling pool;
		

	public FeatureLayer(Convolution con, Pooling pool) {
		this.con = con;
		this.pool = pool;
	}

	public void applyLayer(List<Matrix> data, DoubleUnaryOperator function) {
		convolute(data);
		pool(data);
		activate(data, function);
	}

	private void convolute(List<Matrix> data) {
		List<Matrix> result = new ArrayList<>();
		for (Matrix level : data) {
			result.addAll(con.convolute(level));
		}
		data.clear();
		data.addAll(result);
	}

	private void pool(List<Matrix> data) {
		List<Matrix> result = new ArrayList<>();
		for (Matrix level : data) {
			result.add(pool.pool(level));
		}
		data.clear();
		data.addAll(result);
	}

	private void activate(List<Matrix> data, DoubleUnaryOperator function) {
		for (Matrix level : data) {
			level.applyActivation(function);
		}
	}
	
	public Convolution getCon() {
		return con;
	}

	public void setCon(Convolution con) {
		this.con = con;
	}

	public Pooling getPool() {
		return pool;
	}

	public void setPool(Pooling pool) {
		this.pool = pool;
	}

	public FeatureLayer copy() {
		return new FeatureLayer(con.copy(), pool.copy());
	}
	
}
