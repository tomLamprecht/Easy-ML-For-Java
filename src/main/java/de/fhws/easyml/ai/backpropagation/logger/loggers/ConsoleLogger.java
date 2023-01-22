package de.fhws.easyml.ai.backpropagation.logger.loggers;

import de.fhws.easyml.ai.backpropagation.logger.BackpropagationLogger;
import de.fhws.easyml.ai.neuralnetwork.Backpropagation;

public class ConsoleLogger implements BackpropagationLogger {

    @Override
    public void log(int epoch, Backpropagation.BatchTrainingResult input) {
        System.out.println("EPOCH " + epoch + ": " + input.avg());
    }
}
