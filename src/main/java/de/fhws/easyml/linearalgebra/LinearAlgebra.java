package de.fhws.easyml.linearalgebra;

import java.util.function.DoubleUnaryOperator;


public class LinearAlgebra {

    public static Vector unitVector( int size ) {
        return new Vector( size ).apply( operand -> 1 );
    }

    public static Vector zeroVector( int size ) {
        return new Vector( size ).apply( operand -> 0 ) ;
    }

    public static Vector vectorWithValues( int size, double value) {
        return new Vector( size ).apply( operand -> value );
    }

    public static Vector add(Vector v1, Vector v2) {
        Vector res = v1.copy();
        res.add(v2);
        return res;
    }

    public static Vector sub(Vector v1, Vector v2) {
        Vector res = v1.copy();
        res.sub(v2);
        return res;
    }

    public static Vector apply(Vector vector, DoubleUnaryOperator func) {
        Vector res = vector.copy();
        res.apply(func);
        return res;
    }

    public static Vector multiply(Matrix matrix, Vector vector) {
        if(vector.size() != matrix.getNumCols())
            throw new IllegalArgumentException("vector must have the same size as the matrix has columns");
        Vector res = new Vector(matrix.getNumRows());
        for(int i = 0; i < matrix.getNumRows(); i++) {
            double sum = 0;
            for(int j = 0; j < matrix.getData()[i].length; j++) {
                sum += matrix.getData()[i][j] * vector.get(j);
            }
            res.set(i, sum);
        }
        return res;
    }
    


}
