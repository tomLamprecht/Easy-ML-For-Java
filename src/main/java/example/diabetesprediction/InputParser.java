package example.diabetesprediction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class InputParser {
    private static final Path dataOrigin = Path.of("data/diabetesprediction/diabetes.csv");
    public static final int amountOfUnseenData = 100;

    private final List<DiabetesDataSet> trainingsData;
    private final List<DiabetesDataSet> unseenData;
    private final Random random = new Random();
    private final AtomicBoolean lastOneHadDiabetes = new AtomicBoolean();

    public InputParser() throws IOException {
        List<DiabetesDataSet> inputData = Files.lines(dataOrigin)
                .skip(1) //Skip the headers of the .csv file
                .map(set -> set.split(","))
                .map(DiabetesDataSet::new)
                .collect(Collectors.toList());

        //We don't want our Algorithm to just "remember" the data
        //Therefore we split the data up in trainings data and in unseen data which we can
        //check our result with afterwards
        this.trainingsData = inputData.subList(amountOfUnseenData, inputData.size());
        this.unseenData = inputData.subList(0, amountOfUnseenData);
    }

    public List<DiabetesDataSet> getTrainingsData() {
        return trainingsData;
    }

    public DiabetesDataSet getRandomTrainingsDataSet(){
        //The input data is quite heavily biased on having datasets without diabetes (around 65%)
        //if we don't make sure that the input is roughly 50% our Algorithm will learn that
        //always predicting "no diabetes" has a success rate of 65%
        //We want our Algorithm to be based on the input values though and not on statistical probabilities
        DiabetesDataSet returnValue = trainingsData.get(random.nextInt(trainingsData.size()));
        while(returnValue.hasDiabetes() == lastOneHadDiabetes.get())
            returnValue = trainingsData.get(random.nextInt(trainingsData.size()));
        lastOneHadDiabetes.set(returnValue.hasDiabetes());
        return returnValue;
    }

    public List<DiabetesDataSet> getUnseenData() {
        return unseenData;
    }
}
