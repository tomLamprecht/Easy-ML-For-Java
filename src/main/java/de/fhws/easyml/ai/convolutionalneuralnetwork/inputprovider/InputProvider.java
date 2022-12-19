package de.fhws.easyml.ai.convolutionalneuralnetwork.inputprovider;

import de.fhws.easyml.linearalgebra.Matrix;

import java.util.List;

public interface InputProvider {

    List<Matrix> provide();
}
