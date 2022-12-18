package testCNN;

import de.fhws.easyml.ai.convolutionalneuralnetwork.layer.convolutionlayer.Filter;
import de.fhws.easyml.linearalgebra.Matrix;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TestFilter {

    private static final Matrix BASE_MATRIX = new Matrix(new double[][]{
            {1, 0, 1, 1, 1},
            {1, 1, 0, 0, 1},
            {1, 0, 1, 1, 1}});

    private static final Matrix BASE_MATRIX_BIG = new Matrix(new double[][]{
        {1, 0, 1, 1, 1, 0, 1, 0, 0},
        {0, 1, 0, 1, 0, 1, 1, 0, 0},
        {0, 1, 1, 1, 0, 0, 1, 0, 1},
        {1, 0, 0, 0, 1, 1, 0, 0, 1},
        {0, 1, 1, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 1, 0, 0, 1, 0, 0},
    });

    /*
    x   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    x   0, 0, 0,{1, 0, 1, 1, 1, 0, 1, 0, 0} 0, 0, 0,
        0, 0, 0,{0, 1, 0, 1, 0, 1, 1, 0, 0} 0, 0, 0,
        0, 0, 0,{0, 1, 1, 1, 0, 0, 1, 0, 1} 0, 0, 0,
    x   0, 0, 0,{1, 0, 0, 0, 1, 1, 0, 0, 1} 0, 0, 0,
        0, 0, 0,{0, 1, 1, 0, 0, 0, 0, 0, 1} 0, 0, 0,
        0, 0, 0,{1, 0, 1, 1, 0, 0, 1, 0, 0} 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    }); x              x              x
     */



    private static final Matrix FILTER_DATA = new Matrix(new double[][]{
            {1, 1},
            {0, 0}
    });

    private static final Matrix FILTER_DATA_BIG = new Matrix(new double[][]{
            {1, 0, 0, 1},
            {0, 1, 1, 0},
            {0, 1, 1, 0},
            {1, 0, 0, 1}
    });


    @Test
    public void testFilterNoPaddingNoStriding() {
        Filter filter = new Filter(FILTER_DATA, 1, 1, false);
        double[][] result = filter.doFiltering(BASE_MATRIX).getData();
        double[][] expected = new double[][]{
                {1, 1, 2, 2},
                {2, 1, 0, 1}
        };
        assertEquals(Arrays.deepToString(expected), Arrays.deepToString(result));
    }

    @Test
    public void testFilterWithPaddingNoStriding() {
        Filter filter = new Filter(FILTER_DATA, 1, 1, true);
        double[][] result = filter.doFiltering(BASE_MATRIX).getData();
        double[][] expected = new double[][]{
                {0, 0, 0, 0, 0, 0},
                {1, 1, 1, 2, 2, 1},
                {1, 2, 1, 0, 1, 1},
                {1, 1, 1, 2, 2, 1}
        };
        assertEquals(Arrays.deepToString(expected), Arrays.deepToString(result));
    }

    @Test
    public void testFilterWithPaddingAndStriding() {
        Filter filter = new Filter(FILTER_DATA, 2, 2, true);
        double[][] result = filter.doFiltering(BASE_MATRIX).getData();
        double[][] expected = new double[][]{
                {0, 0, 0},
                {1, 1, 1}
        };
        assertEquals(Arrays.deepToString(expected), Arrays.deepToString(result));
    }

    @Test
    public void testFilterWithPaddingAndStridingBig() {
        Filter filter = new Filter(FILTER_DATA_BIG, 3, 5, true);
        double[][] result = filter.doFiltering(BASE_MATRIX_BIG).getData();
        double[][] expected = new double[][]{
                {1, 1, 0},
                {2, 4, 1},
                {1, 2, 1},
        };
        assertEquals(Arrays.deepToString(expected), Arrays.deepToString(result));
    }
}
