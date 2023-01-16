package de.fhws.easyml.ai.neuralnetwork.costfunction;

import de.fhws.easyml.linearalgebra.Vector;
import de.fhws.easyml.utility.Validator;

public class SummedCostFunction implements CostFunction{

    @Override
    public double costs( Vector expected, Vector actual ) {
        Validator.value( expected.size() ).isEqualToOrThrow( actual.size() );
        double temp = 0;
        for ( int i = 0; i < expected.size(); i++ ) {
            temp += Math.pow( expected.get( i ) - actual.get( i ), 2 );
        }
        return  temp;
    }

    @Override
    public double derivativeWithRespectToNeuron( Vector expected, Vector actual, int indexOfNeuron ) {
        return  2 * ( actual.get( indexOfNeuron ) - expected.get( indexOfNeuron ) );
    }
}
