package testLinearAlgebra;

import de.fhws.easyml.linearalgebra.Matrix;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TestMatrix {

    private static final Matrix BASE_MATRIX = new Matrix(new double[][]{
            {1,2,3},
            {4,5,6},
            {7,8,9}});

    @Test
    public void testSubMatrixInclusive(){
        Matrix subMatrix = BASE_MATRIX.getSubMatrix(0,0,2,2);
        double[][] expectedResult = new double[][]{{1,2},{4,5}};
        assertEquals(Arrays.deepToString(expectedResult), Arrays.deepToString(subMatrix.getData()));
    }

    @Test
    public void testSubMatrixOutOfBound(){
        Matrix subMatrix = BASE_MATRIX.getSubMatrix(1,1,3,4);
        double[][] expectedResult = new double[][]{
                {5,6,0,0},
                {8,9,0,0},
                {0,0,0,0}};
        assertEquals(Arrays.deepToString(expectedResult), Arrays.deepToString(subMatrix.getData()));
    }

    @Test
    public void testSubMatrixNegativeValues(){
        Matrix subMatrix = BASE_MATRIX.getSubMatrix(-1, -1, 5, 3);
        double[][] expectedResult = new double[][]{
                {0,0,0},
                {0,1,2},
                {0,4,5},
                {0,7,8},
                {0,0,0}};
        assertEquals(Arrays.deepToString(expectedResult), Arrays.deepToString(subMatrix.getData()));
    }
}
