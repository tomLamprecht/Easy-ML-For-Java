package de.fhws.networks.convulotionalnn;

import java.util.ArrayList;
import java.util.List;

import de.fhws.linearalgebra.Matrix;
import de.fhws.linearalgebra.Randomizer;
import de.fhws.linearalgebra.Vector;
import de.fhws.networks.ActivationFunction;
import de.fhws.networks.convulotionalnn.convolution.Convolution;
import de.fhws.networks.convulotionalnn.pooling.NonPooling;
import de.fhws.networks.convulotionalnn.pooling.Pooling;
import de.fhws.networks.neuralnetworks.NeuralNet;

public class ConvolutionalNet {

	private final List<FeatureLayer> layers;
	private NeuralNet nn;
	private final ActivationFunction activationFunc;

	private ConvolutionalNet(List<FeatureLayer> layers, ActivationFunction function) {
		this.layers = layers;
		this.activationFunc = function;
	}

	public void setNN(NeuralNet nn) {
		this.nn = nn;
	}

	public Vector calcOutput(List<Matrix> data) {
		Vector ln = learnFeatures(data);
		System.out.println(ln.size());
		ln = nn.calcOutput(ln);
		return ln;
	}

	private Vector learnFeatures(List<Matrix> data) {
		for (FeatureLayer layer : layers) {
			System.out.println("Data size: " + (data.size() * data.get(0).getNumCols() * data.get(1).getNumRows()));
			layer.applyLayer(data, activationFunc);
		}
		return transformMatricesToVector(data);

	}

	private Vector transformMatricesToVector(List<Matrix> data) {
		List<Double> vectorList = new ArrayList<>();
		for (Matrix level : data) {
			for (int i = 0; i < level.getNumRows(); i++) {
				for (int j = 0; j < level.getNumCols(); j++) {
					vectorList.add(level.get(i, j));
				}
			}
		}
		Vector ln = new Vector(vectorList.stream().mapToDouble(d -> d).toArray());
		return ln;
	}

	public List<FeatureLayer> getLayers() {
		return layers;
	}

	public static class Builder {

		private List<FeatureLayer> layers = new ArrayList<>();
		private List<Double> nnHiddenLayers = new ArrayList<>();
		private int outputs;
		private int inputWidth;
		private int inputHeight;
		private int amountInputLevels;
		private ActivationFunction actFunc = d -> (1 + Math.tanh(d / 2)) / 2;
		private Randomizer randFilter = new Randomizer(0, 1);
		private Randomizer randFilterBias = new Randomizer(0, 1);
		private Randomizer randNN;
		private Randomizer randNNBias;

		public Builder(int inputHeight, int inputWidth, int amountInputLevels, int outputs) {
			this.outputs = outputs;
			this.inputWidth = inputWidth;
			this.inputHeight = inputHeight;
			this.amountInputLevels = amountInputLevels;
		}

		public Builder withFeatureLayer(Convolution con, Pooling pool) {
			layers.add(new FeatureLayer(con, pool));
			return this;
		}

		public Builder withFeatureLayer(Convolution con) {
			layers.add(new FeatureLayer(con, new NonPooling()));
			return this;
		}

		public Builder withRandFilter(Randomizer randFilter) {
			this.randFilter = randFilter;
			return this;
		}

		public Builder withRandFilterBias(Randomizer randFilterBias) {
			this.randFilterBias = randFilterBias;
			return this;
		}

		public Builder withRandNN(Randomizer randNN) {
			this.randNN = randNN;
			return this;
		}

		public Builder withRandNNBias(Randomizer randNNBias) {
			this.randNNBias = randNNBias;
			return this;
		}

		public Builder withNNHiddenLayer(double relativeSize) {
			if (relativeSize <= 0)
				throw new IllegalArgumentException("relativeSize must be greater than 0");
			nnHiddenLayers.add(relativeSize);
			return this;
		}

		public Builder withActivationFunction(ActivationFunction function) {
			this.actFunc = function;
			return this;
		}

		public ConvolutionalNet build() {
			ConvolutionalNet cnn = new ConvolutionalNet(layers, actFunc);
			cnn.getLayers().forEach(layer -> layer.getCon().generateRandomizedFilters(randFilter, randFilterBias));
			int nnInputSize = cnn.learnFeatures(createDummyData()).size();
			
			NeuralNet.Builder nnBuilder = new NeuralNet.Builder(nnInputSize, outputs);
			for (double hiddenLayer : nnHiddenLayers) 
				nnBuilder.addLayer((int) (hiddenLayer * nnInputSize));
			
			nnBuilder.withActivationFunction(actFunc);
			if(randNN != null)
				nnBuilder.withWeightRandomizer(randNN);

			if(randNNBias != null)
				nnBuilder.withBiasRandomizer(randNNBias);
			
			NeuralNet nn = nnBuilder.build();
			cnn.setNN(nn);
			return cnn;
		}

		private List<Matrix> createDummyData() {
			List<Matrix> result = new ArrayList<>();
			for (int i = 0; i < amountInputLevels; i++) {
				double[][] matrix = new double[inputHeight][inputWidth];
				result.add(new Matrix(matrix));
			}
			return result;
		}

	}

}
