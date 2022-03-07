package de.fhws.networks.neuralnetworks;


import de.fhws.linearalgebra.Randomizer;
import de.fhws.linearalgebra.Vector;
import de.fhws.networks.ActivationFunction;
import de.fhws.networks.Network;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NeuralNet implements Network<Vector>, Serializable {

    @Serial
    private static final long serialVersionUID = -5984131490435879432L;

    int inputVectorSize;
    private final List<Layer> layers;

    private NeuralNet() {
        layers = new ArrayList<>();
    }

    private NeuralNet(int inputVectorSize, List<Layer> layers) {
        this.inputVectorSize = inputVectorSize;
        this.layers = layers;
    }

    /**
     * calculates the output based on the given input vector
     *
     * @param input vector with the input values; must be the size specified at
     *              built
     * @return the calculated output vector
     * @throws IllegalArgumentException if the size of {@code input} is not the
     *                                  specified one
     */
    public Vector calcOutput(Vector input) {
        if (input.size() != inputVectorSize)
            throw new IllegalArgumentException("input num not equal to given input size");
        for (Layer l : layers) {
            input = l.calcActivation(input);
        }
        return input;
    }

    /**
     * calculates the output based on the given input vector
     *
     * @param input vector with the input values; must be the size specified at
     *              built
     * @return all calculated vectors (hidden layers and output layer)
     * @throws IllegalArgumentException if the size of {@code input} is not the
     *                                  specified one
     */
    public List<Vector> calcAllLayer(Vector input) {
        if (input.size() != inputVectorSize)
            throw new IllegalArgumentException("input num not equal to given input size");
        List<Vector> list = new ArrayList<>(layers.size());
        for (Layer l : layers) {
            input = l.calcActivation(input);
            list.add(input);
        }
        return list;
    }

    public NeuralNet randomize(Randomizer weightRand, Randomizer biasRand) {
        for (Layer l : layers)
            l.randomize(weightRand, biasRand);
        return this;
    }


    public List<Layer> getLayers() {
        return layers;
    }

    /**
     * copy the current NeuralNet
     *
     * @return copy of the current NeuralNet
     */
    public NeuralNet copy() {
        List<Layer> copiedLayers = new ArrayList<>();
        for (int i = 0; i < layers.size(); i++) {
            copiedLayers.add(layers.get(i).copy());
        }
        return new NeuralNet(this.inputVectorSize, copiedLayers);
    }




    public static class Builder {
        private int outputSize;
        private ActivationFunction activationFunction;
        private Randomizer weightRand = new Randomizer(-1, 1);
        private Randomizer biasRand = new Randomizer(0, 1);
        private List<Integer> layerNums = new ArrayList<>();

        /**
         * Constructor to create a Builder which is capable to build a NeuralNet
         *
         * @throws IllegalArgumentException if depth is less or equal 1 or if inputNodes
         *                                  is less than 1
         */
        public Builder(int inputSize, int outputSize) {
            layerNums.add(inputSize);
            this.outputSize = outputSize;
            activationFunction = d -> (1 + Math.tanh(d / 2)) / 2;
        }

        /**
         * set activation function of the neural network. Must be called before adding
         * any layers
         *
         * @param aFunc ActivationFunction (Function that accepts Double and returns
         *              Double) to describe the activation function which is applied on
         *              every layer on calculation
         */
        public Builder withActivationFunction(ActivationFunction aFunc) {
            activationFunction = aFunc;
            return this;
        }

        public Builder withWeightRandomizer(Randomizer weightRandomizer) {
            this.weightRand = weightRandomizer;
            return this;
        }

        public Builder withBiasRandomizer(Randomizer biasRandomizer) {
            this.biasRand = biasRandomizer;
            return this;
        }

        /**
         * adds a layer to the neural network
         *
         * @param numNodes the number of nodes of the added layer
         * @return this
         * @throws IllegalArgumentException if numNodes are 0 or smaller
         */
        public Builder addLayer(int numNodes) {
            if (numNodes <= 0) {
                throw new IllegalArgumentException("numNodes must be at least 1");
            }
            layerNums.add(numNodes);
            return this;
        }

        /**
         * adds the specified amount of layers to the neural network
         *
         * @param amount   the amount of layers added
         * @param numNodes the number of nodes of the added layers
         * @return this
         */
        public Builder addLayers(int amount, int numNodes) {
            for (int i = 0; i < amount; i++) {
                addLayer(numNodes);
            }
            return this;
        }

        /**
         * adds layers of the specified sizes to the neural network
         *
         * @param numNodes array of the number of nodes which are added
         * @return this
         */
        public Builder addLayers(int... numNodes) {
            for (int n : numNodes) {
                addLayer(n);
            }
            return this;
        }

        /**
         * builds the NeuralNet
         *
         * @return the built NeuralNet
         * @throws IllegalStateException
         */
        public NeuralNet build() {
            layerNums.add(outputSize);

            NeuralNet nn = new NeuralNet();
            nn.inputVectorSize = layerNums.get(0);
            int linkedN = nn.inputVectorSize;
            for(int num : layerNums) {
                nn.layers.add(new Layer(num, linkedN, activationFunction));
                linkedN = num;
            }
            return nn.randomize(weightRand, biasRand);
        }

    }

}
