package testLinearAlgebra;

import de.fhws.ai.linearalgebra.LinearAlgebra;
import de.fhws.ai.linearalgebra.Matrix;
import de.fhws.ai.linearalgebra.Vector;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestVector {

    private final Vector vector1 = LinearAlgebra.unitVector( 3 );
    private final Vector vector2 = LinearAlgebra.vectorWithValues( 3, 2 );

    @Test
    public void test_apply( ) {
        Vector actualResult = new Vector( 3 );
        actualResult.apply( operand -> 10 );

        Vector expectedResult = new Vector( 10d, 10d, 10d );

        assertEquals( expectedResult, actualResult );
    }

    @Test
    public void test_add( ) {
        Vector expectedResult = LinearAlgebra.vectorWithValues( 3, 3 );
        Vector actualResult = LinearAlgebra.add( vector1, vector2 );

        assertEquals( expectedResult, actualResult );
    }

    @Test
    public void test_sub( ) {
        Vector expectedResult = LinearAlgebra.vectorWithValues( 3, -1 );
        Vector actualResult = LinearAlgebra.sub( vector1, vector2 );

        assertEquals( expectedResult, actualResult );
    }

    @Test
    public void test_multiply_with_matrix( ) {
        Vector expectedResult = new Vector( 6, 12 );
        Matrix matrix = new Matrix( new double[][]{ { 1, 1, 1 }, { 2, 2, 2 } } );
        Vector actualResult = LinearAlgebra.multiply( matrix, vector2 );

        assertEquals( expectedResult, actualResult );
    }
}
