package de.fhws.easyml.ai.neuralnetwork.activationfunction;

public class Tanh implements ActivationFunction{

    @Override
    public double applyActivation(double x) {
        return Math.tanh(x);
    }

    @Override
    public double derivative(double x) {
        double tanh = applyActivation(x);
        return 1 - (tanh * tanh);
    }
}
