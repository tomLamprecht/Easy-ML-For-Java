package testCNN;

import de.fhws.easyml.ai.convolutionalneuralnetwork.layer.poolinglayer.AvgPoolingLayer;
import de.fhws.easyml.ai.convolutionalneuralnetwork.layer.poolinglayer.MaxPoolingLayer;
import de.fhws.easyml.ai.convolutionalneuralnetwork.layer.poolinglayer.PoolingLayer;
import de.fhws.easyml.linearalgebra.Matrix;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TestPool {

    private static Matrix BASE_MATRIX = new Matrix(new double[][]{
            {1 ,2 ,3 , 4, 5, 6},
            {7 ,8 ,9 ,10,11,11},
            {12,13,14,15,16,17},
            {18,19,20,21,22,23},
            {24,25,26,27,28,29}});

    @Test
    public void testMaxPooling(){
        PoolingLayer poolingLayer = new MaxPoolingLayer(2,2);
        Matrix result = poolingLayer.pool(BASE_MATRIX);
        double[][] expected = new double[][]{
                {8,10,11},
                {19,21,23},
                {25,27,29}};
        assertEquals(Arrays.deepToString(expected),Arrays.deepToString(result.getData()));
    }

    @Test
    public void testAveragePooling(){
        PoolingLayer poolingLayer = new AvgPoolingLayer(1,5);
        Matrix result = poolingLayer.pool(BASE_MATRIX);
        double[][] expected = new double[][]{
                {3,1.2},
                {9,2.2},
                {14,3.4},
                {20,4.6},
                {26,5.8}};
        assertEquals(Arrays.deepToString(expected), Arrays.deepToString(result.getData()));
    }

}
