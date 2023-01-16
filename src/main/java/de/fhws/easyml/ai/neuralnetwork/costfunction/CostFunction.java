package de.fhws.easyml.ai.neuralnetwork.costfunction;

import de.fhws.easyml.linearalgebra.Vector;

public interface CostFunction {

     double costs( Vector expected, Vector actual);

     double derivativeWithRespectToNeuron( Vector expected, Vector actual, int indexOfNeuron );
}
