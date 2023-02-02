

<h1 align="center"> Easy ML for Java</h1>
  <p align="center">
    The easiest way to start with Machine Learning in Java!
    <br/>
    <br />
    <a href="https://easy-ml.gitbook.io/easy-ml-for-java/"><strong>Explore our Website»</strong></a>
    <br />
</p>



## **Version 1.0**
<div>
    <image src="https://img.shields.io/github/repo-size/tomLamprecht/easy-ml-for-java"/>
    <image src="https://img.shields.io/github/contributors/tomLamprecht/easy-ml-for-java"/>
    <image src="https://img.shields.io/github/commit-activity/m/tomLamprecht/easy-ml-for-java"/>
    <image src="https://img.shields.io/github/languages/top/tomLamprecht/easy-ml-for-java"/>
    <image src="https://img.shields.io/snyk/vulnerabilities/github/tomLamprecht/easy-ml-for-java"/>
    
</div>

[![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=Try%20out%20this%20Machine%20Learning%20Framework%20for%20Java%20that%20I%20discovered!&url=https://github.com/tomLamprecht/Easy-ML-For-Java&via=tomLamprecht&hashtags=machinelearning,java,ai,opensource,developers)

---

# Overview

This Framework can be used to implement a Machine Learning Algorithm.\
It uses *Neural Networks* which are beeing trained by a *Genetic Algorithm* \
The motivation for this Framework was that many universities and schools teach as a first language Java to their students. Sadly for Java there aren't many Machine Learning Libraries and Frameworks out there, and if there are you most likely don't want to spend months trying to learn how to use it just to try out some stuff.\
Hence, Easy ML for Java:\
It's easy to use and designed to be played around with. You can choose the settings of the training process and see how this affects the outcome.
There is no wrong way to use Easy ML for Java!


- [Get Started](#get-started)

- [Example](#example)

- [Classes and Interfaces of the Neural Network](#classes-nn)
	- [NeuralNet](#NeuralNet)
	
- [Classes and Interfaces of the Genetic Algorithm](#classes-ga)
	- [Individual](#Individual)
	- [PopulationSupplier](#PopulationSupplier)
	- [Selector](#Selector)
	- [Recombiner](#Recombiner)
	- [Mutator](#Mutator)
	- [Logger](#logger)
		- [GraphPlotLogger](#graph-logger)
	- [GeneticAlgorithm](#genetic-algorithm)
	
- [Classes and Interfaces of the NetworkTrainer](#NetworkTrainer)
	- [NeuralNetFitnessFunction](#NeuralNetFitnessFunction)
	- [NeuralNetIndividual](#NeuralNetIndividual)
	- [NeuralNetPopulationSupplier](#NeuralNetPopulationSupplier)
	
- [License & Copyright](#license)

<br>

<a name="get-started"></a>
# Get Started

To create your own AI you first need to have at least a basic understanding of the below listet classes.
To implement an AI for your own personal problem you will have to follow these steps:
1. Design the structure of the [NeuralNet](#NeuralNet) using the NeuralNetBuilder
2. implement a [NeuralNetFitnessFunction](#NeuralNetFitnessFunction)
3. Choose and configure the values of [Selector](#Selector), [Recombiner](#Recombiner) and [Mutator](#Mutator)
4. Configure the parameters of the [Genetic Algorithm](#genetic-algorithm)
5. combine everything in the [GeneticAlgorithm](#genetic-algorithm).Builder class
>Finding the correct configurations is a very challenging task and you should always use [Loggers](#logger) to help you find the correct ones! 

An abstract example could look like this:
```java
        Selector<NeuralNetIndividual> SELECTOR = new EliteSelector<>( 0.1 );
    Recombiner<NeuralNetIndividual> RECOMBINER = new NNUniformCrossoverRecombiner( 2 );
    Mutator<NeuralNetIndividual> MUTATOR = new NNRandomMutator( 0.9, 0.4, new Randomizer( -0.01, 0.01 ), 0.01 );
    int GENS = 50;
    NeuralNetFitnessFunction FITNESS_FUNCTION = (nn) -> //calculate Fitness; 

    NeuralNetSupplier neuralNetSupplier = ( ) -> new NeuralNet.Builder( NEURALNET_INPUT_SIZE, NEURALNET_OUTPUT_SIZE )
                .addLayer( HIDDEN_LAYER_SIZE )
                .withActivationFunction( x -> x )
                .build( );
                
        NeuralNetPopulationSupplier supplier = new NeuralNetPopulationSupplier( neuralNetSupplier, FITNESS_FUNCTION, POP_SIZE );

  GeneticAlgorithm<NeuralNetIndividual> geneticAlgorithm =
                new GeneticAlgorithm.Builder<>( supplier, GENS, SELECTOR )
                        .withRecombiner( RECOMBINER )
                        .withMutator( MUTATOR )
                        .build( );
    
    geneticAlgorithm.solve();

```


---
<a name="example"></a>
# EXAMPLE

This example will show how to use the Framework for a number prediction based on the simple mathematic equation: f(x) = 2x\
[EXAMPLE](src/main/java/example/SimpleFunctionPredictionExample.java)

In this example the framework is used to predict if someone has diabetes or not based on given trainingsdata.\
[DIABETES EXAMPLE](src/main/java/example/diabetesprediction/Main.java)
> You can find a full tutorial for this example on our [Website](https://easy-ml.gitbook.io/easy-ml-for-java/fundamentals/implement-your-first-ai)

This one includes a basic implementation of the game "Snake" and will show the use of a more complex scenario of the Framework.\
[SNAKE EXAMPLE](src/main/java/example/SnakeGameExample/snakegame/Main.java)

---
<a name="classes-nn"></a>
## Classes and Interfaces of the Neural Network:
<a name="NeuralNet"></a>
## NeuralNet

This is the "brain" of the Aritfical Intelligence. For further information about the way this *Neural Network* works read the [Wikipedia-Article](https://en.wikipedia.org/wiki/Artificial_neural_network) about it.\
To create a NeuralNet you need to use Builder of this class. 
It is necessary to at least provide the input and the output size of this Network. Its recommended though to also provide at least 1 hidden layer for more complex problems.
A Example for a Neural Network with the input size 100 output size 2 and a hidden layer with 50 nodes could look like this:
```java
NeuralNet neuralNet = new NeuralNet.Builder( 100, 12 )
                .addLayer( 50 )
                .build()
```
\
with the method
```java
withActivationFunction(...)
```
you can provide your own Activation Function. The default one is

$$
f(x) =  \frac{1 + tanh\Bigl(\frac{x}{2}\Bigr)}{2}
$$ 

\
The most simple one would be:

```java
withActicationFunction(x -> x)
```

---


<a name="classes-ga"></a>

## Classes and Interfaces of the Genetic Algorithm:
<a name="Individual"></a>
## Individual

A Indivdual displays one Element in the Population of the Genetic Algorithm. The implementation details may differ from problem to problem and have to be therefor implemented by the user. To support the programmer with this task the Framework provides a basic structure to implement a Individual.

This is a simplified Version of the Individual interface
```java
public interface Individual<T extends Individual<T>>{
	
     void calcFitness();
     
     double getFitness();
     
     T copy();
```
In your implementation of Invdividual <u>T should always be the implementing Class itself </u>\
e.g:
```java
public class IndividualImplementation extends Individual<IndividualImplementation>{...}
```
<a name="calcFitness"></a>
- ```void calcFitness()```\
This method will be used to calculate a Fitness value. Basic Rule here is: The higher the Fitness value the better the Individual!

- ```double getFitness()```\
Should return the fitness value calculated in the calcFitness Method. Due to Performance issues the calculation of the Fitness Value should <b>always</b> be outsourced to the ```calcFitness()``` method. 


- ```T copy()``` \
The copy method should be self explanatory: provides a copy of the Individual without sharing any rerences of to the Individual itself


---    
<a name="PopulationSupplier"></a>
## PopulationSupplier

The Population Supplier is used to supply the Genetic Algorithm with a Population of Individuals. A example of a PopulationSupplier using Java 8 Lambda expression could looks like this:
```
() -> new Population(//List of Individuals);
```

The Framework already provides a Implementation of a PopulationSupplier to read a Population from a File:\
[PopulationByFileSupplier](src/main/java/de/fhws/easyml/easyml/geneticalgorithm/populationsupplier/PopulationSupplier.java)

---
<a name="Selector"></a>
## Selector
The task of the Selector is to throw out the worst Individuals of the population based on their fitness values. There are many ways to achieve such a selection using differnt mathematical approaches. \
This Framework provides 3 of them:
* [EliteSelector](src/main/java/de/fhws/easyml/easyml/geneticalgorithm/evolution/selectors/EliteSelector.java) ([Wikipedia](https://en.wikipedia.org/wiki/Selection_(genetic_algorithm)#Elitism_Selection))
* [RouletteWheelSelector](src/main/java/de/fhws/easyml/easyml/geneticalgorithm/evolution/selectors/RouletteWheelSelector.java) ([Wikipedia](https://en.wikipedia.org/wiki/Selection_(genetic_algorithm)#Roulette_Wheel_Selection))
* [TournamentSelector](src/main/java/de/fhws/easyml/easyml/geneticalgorithm/evolution/selectors/TournamentSelector.java) ([Wikipedia](https://en.wikipedia.org/wiki/Selection_(genetic_algorithm)#Tournament_Selection))

It is also possible to provide your own implementation though. To do this you will have to Implement the [Selector-Interface](/de/fhws/strategies/geneticalgorithm/evolution/selectors/Selector.java). Before starting to write your own Selector you may want to look into the implementation of the already given Selectors first.\
A possible EliteSelector could look like this:
```java
new EliteSelector<>( 0.1 );
```

---
<a name="Recombiner"></a>
## Recombiner
The Recombiner may be used to fill up the Popluation again after the selection process.
Again there are many different approaches.\
This framework provides 2 of them:
* [FillUpRecombiner](/de/fhws/strategies/geneticalgorithm/evolution/recombiners/FillUpRecombiner.java) (just fills up the Population with copies of the remaining Individuals)
* [NNUniformCrossoverRecombiner](/de/fhws/easyml/ai/geneticneuralnet/NNUniformCrossoverRecombiner.java) ([Wikipedia](https://en.wikipedia.org/wiki/Crossover_(genetic_algorithm)#Uniform_crossover))

It's obviously also possible to provide your own Recombiner by Implementing the [Recombiner-Interface](/de/fhws/strategies/geneticalgorithm/evolution/recombiners/Recombiner.java) \
A possible NNUniformCrossOverRecombiner could look like:
```java
new NNUniformCrossoverRecombiner( 2 );
```

---
<a name="Mutator"></a>
## Mutator
After the Selection and Recombination process its possible to provide a third processing step called *Mutator*. Some Individuals may randomly get changed to increase the probability of a positive mutation. \
This framework provides one of them:
* [NNRandomMutator](/de/fhws/easyml/ai/geneticneuralnet/NNRandomMutator.java) ([Wikipedia](https://en.wikipedia.org/wiki/Mutation_(genetic_algorithm)))

As always it's possible to provide your own Mutator by implementing the [Mutator-Interface](/de/fhws/strategies/geneticalgorithm/evolution/Mutator.java).
\
A possible NNRandomMutator could look like:
```java
new NNRandomMutator( 0.2, 0.4, new Randomizer( -0.01, 0.01 ), 0.01 );
```
---
<a name="logger"></a>
## Logger
This Interface is just used to log the huge amount of metadata that is beeing generated in the evolution process. There are 3 Implementations of this Interface already given:
* [ConsoleLogger](/de/fhws/strategies/geneticalgorithm/logger/ConsoleLogger.java)  (Prints Metadata in console)
* [IntervalConsoleLogger](/de/fhws/strategies/geneticalgorithm/logger/IntervalConsoleLogger.java) (Prints Metadata in a interval in the console)
* [GraphPlotLogger](/de/fhws/ai/geneticalgorithm/logger/loggers/graphplotter/GraphPlotLogger.java) (Creates a .xls file with a chart of the Fitness Values)

<a name="graph-logger"></a>
#### GraphPlotLogger
takes as arguements in the Constructor
1. <u>Plotting Interval</u> as int - the interval in which the file gets created not in which the data is beeing logged
2. <u>Filename</u> as String - Name of the file without file-ending
3. <u>Chart Title</u> as String *(Optional)* - Title of the resulting chart, default: "Plot for Population size: {size}"
4. <u>Line Generators</u> as LineGenerator[] - The parser for metadata into plottable double values

<b>LineGenerator</b>
is a abstract class that is used to parse a Population into a single double value so its plottable. You may implement your own LineGenerators but the Framework provides 4 of them: 
* AvgFitnessLine - parses the Population to its average Fitness value
* MaxFitnessLine - parses the Population to its maximum Fitness value
* MinFitnessLine - parses the Population to its minimum Fitness value
* NQuantilFitnessLine - parses the Population into its Fitness Value of its n-quantil

A possible GraphPlotLogger could look like this:
```java
int plottingInterval = 100;
double quantilOf20Percent = 0.2
double quantilOf80Percent = 0.8
new GraphPlotLogger(plottingInterval, "plot",  
 new AvgFitnessLine(),  
 new MaxFitnessLine(),  
 new WorstFitnessLine(),  
 new NQuantilFitnessLine(quantilOf20Percent),  
 new NQuantilFitnessLine(quantilOf80Pecent))
```
a resulting graph may look like this:
![image](/plots/exampleplot/examplePlot.PNG)



---
<a name="genetic-algorithm"></a>
## Genetic Algorithm

To create your own *Genetic Algorithm* you will need to build one using the [GeneticAlgorithm.Builder](/de/fhws/strategies/geneticalgorithm/GeneticAlgorithm.java) Class.This class can be given the many optional parameters, the most essential ones will be listed below:

- [PopulationSupplier](#PopulationSupplier) - provides the first Population
- Generations - Amount of generations that should be calculated
- [Selector](#Selector) - Is used for the Selection process
- [Recombiner](#Recombiner) - Is used for the recombination process *(Optional)*
- [Mutator](#Mutator) - Is used for the mutation process *(Optional)* \
A abstract Example for a GeneticAlgorithm could look like this: 
```java
 new GeneticAlgorithm.Builder<>( POPULATION_SUPPLIER, GENS_AMOUNT, SELECTOR )
                        .withRecombiner( RECOMBINER )
                        .withMutator( MUTATOR )
                        .build( );
```
With the ```.withMutliThreaded(//amount of Threads);``` method it's also possible to processes the evolution in a parallel matter. 

---
<a name="NetworkTrainer"></a>
## Classes and Interfaces of the NetworkTrainer
**Combination of Genetic Algorithm and Neural Network**

<a name="NeuralNetFitnessFunction"></a>
## NeuralNetFitnessFunction
This Interface provides one Method
```java
 double calculateFitness( NeuralNet neuralNet );
```
which is used to calculate the Fitness Values of a NeuralNetIndividual.\
See [Individual.calcFitness()](#calcFitness)

---
<a name="NeuralNetIndividual"></a>
## NeuralNetIndividual

This is the combination between a NeuralNet and a Individual.
The constructor of this class therefor takes a [NeuralNet](#NeuralNet) and a [NeuralNetFitnessFunction](#NeuralNetFitnessFunction) to create a *NeuralNetIndividual*.

---
<a name="NeuralNetPopulationSupplier"></a>
## NeuralNetPopulationSupplier
This class provides a Population of NeuralNetIndviduals, using
* a Supplier of a [NeuralNet](#NeuralNet) 
* a [NeuralNetFitnessFunction](#NeuralNetFitnessFunction)
* the size of the population as int\
e.g:
```java
 NeuralNetPopulationSupplier supplier = new NeuralNetPopulationSupplier( () -> new NeuralNet.Builder..., (nn) -> /*calculate Fitness*/, POP_SIZE );
```

---






<a name="license"></a>

# License & Copyright

© Tom Lamprecht, David Kupper - FHWS Fakultät Informatik
