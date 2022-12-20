package de.fhws.easyml.ai.convolutionalneuralnetwork.layer;

import de.fhws.easyml.linearalgebra.Matrix;

import java.util.List;

public interface Layer {

   List<Matrix> process(List<Matrix> input);

   OutputSizeInformation outputSizeInformation(OutputSizeInformation prevLayer);
}
