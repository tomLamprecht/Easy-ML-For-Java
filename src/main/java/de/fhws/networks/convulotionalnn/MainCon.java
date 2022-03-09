package de.fhws.networks.convulotionalnn;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.fhws.linearalgebra.Matrix;
import de.fhws.linearalgebra.Vector;
import de.fhws.networks.convulotionalnn.convolution.ConvolutionalStriding;
import de.fhws.networks.convulotionalnn.pooling.MaxPooling;
import de.fhws.networks.convulotionalnn.pooling.Pooling;

public class MainCon {

	
	
	
	public static void main(String[] args) throws IOException {
		Pooling pool = new MaxPooling(2);
		List<Matrix> list = new LinkedList<>();
		double[][] matrix = new double[253][320];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = (int)(Math.random()*2);
			}
		}
		Matrix data = new Matrix(matrix);
		list.add(data);
		list.add(data);
		list.add(data);
		ConvolutionalNet cnn = new ConvolutionalNet.Builder(data.getNumCols(), data.getNumRows(), 3, 10)
				.withFeatureLayer(new ConvolutionalStriding(4, 2, 2), pool)
				.withFeatureLayer(new ConvolutionalStriding(3, 2, 2), pool)
				.withFeatureLayer(new ConvolutionalStriding(3, 2, 2), pool)
				.withNNHiddenLayer(0.5)
				.build();
		
		long start = System.currentTimeMillis();
		Vector result = cnn.calcOutput(list);
		long end = System.currentTimeMillis();
		long diff = end-start;
		System.out.println(diff);
		BufferedWriter bw = new BufferedWriter(new FileWriter("outputTime.txt", true));
		bw.write("the time taken was: " + diff +'\n') ;
		bw.flush();
		//		System.out.println(result);
		System.out.println(result);
	}
	
}

