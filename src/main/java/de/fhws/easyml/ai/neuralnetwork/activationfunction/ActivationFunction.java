package de.fhws.easyml.ai.neuralnetwork.activationfunction;

import java.io.Serializable;
import java.util.function.DoubleUnaryOperator;

@FunctionalInterface
public interface ActivationFunction extends DoubleUnaryOperator, Serializable {
    double applyActivation( double x );

    @Override
    default double applyAsDouble( double x ) {
        return applyActivation( x );
    }

    default double derivative( double x){
      throw new UnsupportedOperationException();
    }
}
