---
description: A Machine Learning approach in predicting diabetes
---

# 🤖 Implement your first AI

We will go through the whole process of an implementation for this Framework:

## The Problem

Firstly, we have to think about the Problem we want to solve:\
For this Example we will try to predict if someone has diabetes based on some simple input

* Pregnancies
* Glucose
* BloodPressure
* SkinThickness
* Insulin
* BMI
* DiabetesPedigreeFunction
* Age

The needed dataset can be downloaded here

{% embed url="https://www.kaggle.com/code/sandragracenelson/diabetes-prediction/data" %}
Diabetes Dataset
{% endembed %}

{% hint style="info" %}
The data can - just as any other source code - also be found in the GitHub Repository
{% endhint %}

## <mark style="color:orange;">Step 1. Input Parsing</mark>

### Data Object

Let's start with parsing the Input in a Java Class. For this, we create a class called `DiabetesDataSet.` It will be used to hold the data.

<pre class="language-java"><code class="lang-java">public class DiabetesDataSet {
    private final int pregnancies;
    private final int glucose;
    private final int bloodPressure;
    private final int skinThickness;
    private final int insulin;
    private final double bmi;
    private final double diabetesPedigreeFunction;
    private final int age;
    private final boolean diabetes;

    public DiabetesDataSet(String[] values){
        pregnancies = Integer.parseInt(values[0]);
        glucose = Integer.parseInt(values[1]);
        bloodPressure = Integer.parseInt(values[2]);
        skinThickness = Integer.parseInt(values[3]);
        insulin = Integer.parseInt(values[4]);
        bmi = Double.parseDouble(values[5]);
        diabetesPedigreeFunction = Double.parseDouble(values[6]);
        age = Integer.parseInt(values[7]);
        diabetes = values[8].equals("1");
    }
    
    public Vector toVector(){
        return new Vector(pregnancies,
            glucose,
            bloodPressure,
            skinThickness,
            insulin,
            bmi,
            diabetesPedigreeFunction,
            age);
<strong>    }
</strong><strong>    
</strong><strong>    //Getter and Setter...
</strong>}
</code></pre>

{% hint style="info" %}
The `toVector()` Method is used to parse our Object into something that a neural Network can take as Input. The constructor of Vector takes double varags.
{% endhint %}

### Reading the Input

As the next step we will read the data from the file.\
In Java, there are several ways to read data from a File. Since Java 8 we do have the convenient method `Files.lines(...)` though, which returns us a Stream of each line of the file. Using this the resulting class `InputParser` may look like this:

> ```java
> public class InputParser {
>     private static final Path dataOrigin = Path.of(<source>);
>     public static final int amountOfUnseenData = 100;
>
>     private final List<DiabetesDataSet> trainingsData;
>     private final List<DiabetesDataSet> unseenData;  
>
>     public InputParser() throws IOException {
>         List<DiabetesDataSet> inputData = Files.lines(dataOrigin)
>                 .skip(1) //Skip the headers of the .csv file
>                 .map(set -> set.split(","))
>                 .map(DiabetesDataSet::new)
>                 .collect(Collectors.toList());
>
>         //We don't want our Algorithm to just "remember" the data
>         //Therefore we split the data up in trainings data and in unseen data,
>         //with which we can check our result afterwards
>         this.trainingsData = inputData.subList(amountOfUnseenData, inputData.size());
>         this.unseenData = inputData.subList(0, amountOfUnseenData);
>     }
>     //Getter and Setter
> }
> ```

Let's add one more method that returns us one random element from our trainings data:

```java
private final AtomicBoolean lastOneHadDiabetes = new AtomicBoolean();
private final Random random = new Random();

public DiabetesDataSet getRandomTrainingsDataSet(){
   DiabetesDataSet returnValue = trainingsData.get(random.nextInt(trainingsData.size()));
   while(returnValue.hasDiabetes() == lastOneHadDiabetes.get())
       returnValue = trainingsData.get(random.nextInt(trainingsData.size()));
   lastOneHadDiabetes.set(returnValue.hasDiabetes());
   return returnValue;
}
```

{% hint style="info" %}
The input data is quite heavily biased on having datasets without diabetes (around 65%)\
If we don't make sure that the input is roughly 50% our Algorithm will learn that always predicting "no diabetes" has a success rate of 65%\
We want our Algorithm to be based on the input values though and not on statistical probabilities
{% endhint %}

## <mark style="color:orange;">Step 2. Defining a Fitness Function</mark>

The next thing is to define the Fitness Function for our Diabetes Prediction. This is actually much easier than it may seem. Considering there are only 2 outcomes (has diabetes or not) we may only return 1 or 0 as fitness values.

```java
final InputParser inputParser = new InputParser();

NeuralNetFitnessFunction fitnessFunction = (nn) -> {
    DiabetesDataSet data = inputParser.getRandomTrainingsDataSet();
    //Due to the default activation function in the Neural Network the output is between 0 and 1
    //If the output is greater than 0.5 we will interpret this as "Patient has diabetes"
    boolean prediction = nn.calcOutput(data.toVector()).get(0) > 0.5;
    //If the prediction was correct return 1 otherwise return 0
    return  prediction == data.hasDiabetes() ? 1 : 0; 
};
```

{% hint style="info" %}
nn is a instance of a`NeuralNet`. The `calcOutput(...)` method will return us a Vector. In this model the Vector has the size 1 and only contains 1 value - our prediction &#x20;
{% endhint %}

## <mark style="color:orange;">Step 3. Create and Configure the Algorithm</mark>

### The Brain (Neural Network)

To create a brain for our Machine Learning Algorithm, we will have to create a `NeuralNetSupplier` using the `Builder` of the `NeuralNet` class.

<pre class="language-java"><code class="lang-java">NeuralNetSupplier neuralNetSupplier = ( ) -> 
<strong>    new NeuralNet.Builder( 8, 1 )
</strong><strong>//  .addLayer(4)
</strong><strong>    .build( );
</strong></code></pre>

{% hint style="info" %}
This Neural Network will have a input size of 8, no hidden layers, and a output size of 1.\
To add a hidden layer of size n just add`.addLayer(n)` to the code
{% endhint %}

### NeuralNetSupplier

This class will provide our Algorithm with its first population. We did actually already implement everything we need for this:

```java
public static final int POP_SIZE = 1000;
//...
NeuralNetPopulationSupplier nnPopSup = new NeuralNetPopulationSupplier(neuralNetSupplier, fitnessFunction, POP_SIZE);
```

### Selector, Recombiner and Mutator

We now have to decide which implementations we want to use for those Interfaces. Feel free to alter the values or choose even completely other implementations!

```java
private static final Selector<NeuralNetIndividual> SELECTOR = new EliteSelector<>( 0.3 );
private static final Recombiner<NeuralNetIndividual> RECOMBINER = new NNUniformCrossoverRecombiner(2);
private static final Mutator<NeuralNetIndividual> MUTATOR = new NNRandomMutator( 0.3, 0.10, new Randomizer( -0.5, 0.5 ), 0.01 );
```

> If you dont know what they do this is no Problem. Just copy them and try out some stuff on your own. You cant break anything, all implementations work with every other!

### Genetic Algorithm

Now we can put everything together with the Builder of the `GeneticAlgorithm` class

```java
public static final int GENS = 2000;
//...
GeneticAlgorithm<NeuralNetIndividual> geneticAlgorithm = new GeneticAlgorithm.Builder<>(nnPopSup, GENS, SELECTOR)
        .withRecombiner(RECOMBINER)
        .withMutator(MUTATOR)
        .withMultiThreaded(16) //uses 16 Threads to process
        .withLoggers(new IntervalConsoleLogger(100)) //used to print logging info in the console every 100th generation
        .build();
```

{% hint style="info" %}
The `GENS` field will decide how many generations the trainings process will take. But be carefull there is such thing called overfitting which can happen if the algorithm is trained too long on the sample data.\
So more is not necessarily better!&#x20;
{% endhint %}

With `geneticAlgorithm.solve()` we finally start the solving process!\
As a return value, we get our trained Neural Network.&#x20;

## <mark style="color:orange;">Step 4. Check the result</mark>

Hurray you wrote your first AI! But wait, so far we only trained it and have not used it yet! Exactly for this, we saved 100 unseen datasets earlier in this tutorial. \
Let's write a method that tests our new trained Neural Network:

```java
public static void testModel(InputParser inputParser, NeuralNet model){
    long correctGuesses = inputParser.getUnseenData()
            .stream()
            .filter(data -> {
                boolean prediction = model.calcOutput(data.toVector()).get(0) > 0.5;
                System.out.println("prediction is " + prediction + " - patient has diabetes: " + data.hasDiabetes());
                return prediction == data.hasDiabetes();
            })
            .count();
    System.out.println("The model guessed " + ((100d*correctGuesses)/InputParser.amountOfUnseenData) + "% correct");
}
```

It basically works similar to our Fitness Function we wrote earlier. The only difference is that we are counting the correct results, and we added some extra logging as well.

## <mark style="color:orange;">Step 5. Run the code</mark>

It's finally time to execute our code. To do so, just call the `geneticAlgorithm.solve()` method and give its result to our test method:

```java
NeuralNetIndividual result = geneticAlgorithm.solve();
testModel(inputParser, result.getNN());
```

\
For me the test method always yielded an accuracy of roughly 65%- 70%.&#x20;

```
The model guessed 67.0% correct
```

{% hint style="info" %}
Notice though that a Genetic Algorithm is based on randomness, results may therefore highly differ from each time you execute the code!&#x20;
{% endhint %}







{% embed url="https://github.com/tomLamprecht/Easy-ML-For-Java/tree/master/src/main/java/example/diabetesprediction" %}
Download the source code here
{% endembed %}
