package de.fhws.easyml.ai.neuralnetwork.activationfunction;

public class Sigmoid implements ActivationFunction{
    @Override
    public double applyActivation( double d ) {
        return ( 1 + Math.tanh( d / 2 ) ) / 2;
    }

    @Override
    public double derivative( double x ) {
        return applyActivation( x ) * ( 1 - applyActivation( x ));
    }
}
