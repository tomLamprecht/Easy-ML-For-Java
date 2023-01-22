package de.fhws.easyml.ai.backpropagation;

import de.fhws.easyml.ai.backpropagation.logger.BackpropagationLogger;
import de.fhws.easyml.ai.neuralnetwork.Backpropagation;
import de.fhws.easyml.ai.neuralnetwork.NeuralNet;
import de.fhws.easyml.ai.neuralnetwork.costfunction.CostFunction;
import de.fhws.easyml.ai.neuralnetwork.costfunction.SummedCostFunction;
import de.fhws.easyml.linearalgebra.Vector;

import java.util.List;
import java.util.function.Supplier;

public class BackpropagationTrainer {

    private final NeuralNet neuralNet;
    private final Supplier<Batch> batchSupplier;
    private final double learningRate;
    private final int epochs;
    private final CostFunction costFunction;
    private final BackpropagationLogger logger;


    private BackpropagationTrainer(NeuralNet neuralNet, Supplier<Batch> batchSupplier, double learningRate, int epochs, CostFunction costFunction, BackpropagationLogger logger) {
        this.neuralNet = neuralNet;
        this.batchSupplier = batchSupplier;
        this.learningRate = learningRate;
        this.epochs = epochs;
        this.costFunction = costFunction;
        this.logger = logger;
    }



    public void train() {
        for (int i = 0; i < epochs; i++) {
            doEpoch(i, batchSupplier.get());
        }
    }

    private void doEpoch(int i, Batch batch) {
        Backpropagation.BatchTrainingResult result = new Backpropagation(neuralNet).trainBatch(batch.getInputs(), batch.getExpectedOutputs(), costFunction, learningRate);
        logIfPresent(i, result);
    }

    private void logIfPresent(int i, Backpropagation.BatchTrainingResult result) {
        if (logger != null)
            logger.log(i, result);
    }

    public static class Builder {

        private final NeuralNet neuralNet;
        private final Supplier<Batch> batchSupplier;
        private final double learningRate;
        private final int epochs;
        private CostFunction costFunction = new SummedCostFunction();
        private BackpropagationLogger logger;

        /**
         * Builder for Training a Neural Network using a backpropagation approach
         * @param batchSupplier supplies for each epoch a new Batch
         * @param learningRate is the factor on how much impact one batch should have on the weights
         * @param epochs are the amount of how many batches are getting trained
         */
        public Builder(NeuralNet neuralNet, Supplier<Batch> batchSupplier, double learningRate, int epochs) {
            this.neuralNet = neuralNet;
            this.batchSupplier = batchSupplier;
            this.learningRate = learningRate;
            this.epochs = epochs;
        }

        public Builder withCostFunction(CostFunction costFunction) {
            this.costFunction = costFunction;
            return this;
        }

        public Builder withLogger(BackpropagationLogger logger) {
            this.logger = logger;
            return this;
        }

        public BackpropagationTrainer build() {
            return new BackpropagationTrainer(neuralNet, batchSupplier, learningRate, epochs, costFunction, logger);
        }

    }

    public static class Batch{
        private final List<Vector> inputs;
        private final List<Vector> expectedOutputs;

        public Batch(List<Vector> inputs, List<Vector> expectedOutputs) {
            this.inputs = inputs;
            this.expectedOutputs = expectedOutputs;
        }

        public List<Vector> getInputs() {
            return inputs;
        }

        public List<Vector> getExpectedOutputs() {
            return expectedOutputs;
        }
    }


}
