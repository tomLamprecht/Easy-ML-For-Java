# AI-Framework for Java



## **Version 1.0**


---

# Overview

This Framework can be used to implement a Maschine Learning Algorithm.\
It uses *Neural Networks* than can be described by the user of this Framework and an *Genetic Algorithm* to train those Networks. All Parameters can be choosen freely.

- [Usage](#usage)
    - [Start the Program](#start-program) 

- [Installation Guide](#install-guide)

- [License & Copyright](#license)

<br>

---
## Classes and Interfaces of the Neural Network:

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
```
withActivationFunction(...)
```
you can provide your own Activation Function. The default one is (1 + tanh(x / 2))/2 
\
The most simple one would be:

```
withActicationFunction(x -> x)
```

---


<a name="classes"></a>

## Classes and Interfaces of the Genetic Algorithm:

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
- ```void calcFitness()```\
This method will be used to calculate a Fitness value. Basic Rule here is: The higher the Fitness value the better the Individual!

- ```double getFitness()```\
Should return the fitness value calculated in the calcFitness Method. Due to Performance issues the calculation of the Fitness Value should <b>always</b> be outsourced to the ```calcFitness()``` method. 


- ```T copy()``` \
The copy method should be self explanatory: provides a copy of the Individual without sharing any rerences of to the Individual itself


---    

## PopulationSupplier

The Population Supplier is used to supply the Genetic Algorithm with a Population of Individuals. A example of a PopulationSupplier using Java 8 Lambda expression could looks like this:
```
() -> new Population(//List of Individuals);
```

The Framework already provides a Implementation of a PopulationSupplier to read a Population from a File:\
PopulationByFileSupplier

---
## Selector
The task of the Selector is to throw out the worst Individuals of the population based on their fitness values. There are many ways to achieve such a selection using differnt mathematical approaches. \
This Framework provides 3 of them:
* EliteSelector ([Wikipedia](https://en.wikipedia.org/wiki/Selection_(genetic_algorithm)#Elitism_Selection))
* RouletteWheelSelector ([Wikipedia](https://en.wikipedia.org/wiki/Selection_(genetic_algorithm)#Roulette_Wheel_Selection))
* TournamentSelector ([Wikipedia](https://en.wikipedia.org/wiki/Selection_(genetic_algorithm)#Tournament_Selection))

It is also possible to provide your own implementation though. To do this you will have to Implement the Selector-Interface (TODO add Link here). Before starting to write your own Selector you may want to look into the implementation of the already given Selectors first.

---
## Recombiner
The Recombiner may be used to fill up the Popluation again after the selection process.
Again there are many different approaches.\
This framework provides 2 of them:
* FillUpRecombiner (just fills up the Population with copies of the remaining Individuals)
* NNUniformCrossoverRecombiner ([Wikipedia](https://en.wikipedia.org/wiki/Crossover_(genetic_algorithm)#Uniform_crossover))

It's obviously also possible to provide your own Recombiner by Implementing the Recombiner-Interface(Todo add link here)

---
## Mutator
After the Selection and Recombination process its possible to provide a third processing step called *Mutator*. Some Individuals may randomly get changed to increase the probability of a positive mutation. \
This framework provides one of them:
* NNRandomMutator ([Wikipedia](https://en.wikipedia.org/wiki/Mutation_(genetic_algorithm)))

As always it's possible to provide your own Mutator by implementing the Mutator-Interface(TODO add link here)

---
<a name="genetic-algorith"></a>

## Genetic Algorithm

To create your own *Genetic Algorithm* you will need to build one using the GeneticBuilder Class (TODO: insert shortcut here).This class can be given the many optional parameters, the most essential ones will be listed below:

- PopSupplier - provides the first Population
- Generations - Amount of generations that should be calculated
- Selector - Is used for the Selection process
- Recombiner - Is used for the recombination process *(Optional)*
- Mutator - Is used for the mutation process *(Optional)*
---

## Classes and Interfaces of the NetworkTrainer
**Combination of Genetic Algorithm and Neural Network**


## NeuralNetFitnessFunction
This Interface provides one Method
```
 double calculateFitness( NeuralNet neuralNet );
```
which is used to calculate the Fitness Values of a NeuralNetIndividual.\
See (ADD LINK TO FITNESS FUNTION)

---

## NeuralNetIndividual

This is the combination between a NeuralNet and a Individual. \
The constructor of this class therefor takes a NeuralNet and a NeuralNetFitnessFunction to create a *NeuralNetIndividual*.
---

## NeuralNetPopulationSupplier
This class provides a Population of NeuralNetIndviduals, using
* a Supplier of a NeuralNet 
* a NeuralNetFitnessFunction
* the size of the population as int\
e.g:
```java
 NeuralNetPopulationSupplier supplier = new NeuralNetPopulationSupplier( () -> new NeuralNet.Builder..., (nn) -> //calculate Fitness, POP_SIZE );
```

---

# Get Started

To create your own AI you first need to have at least a basic understanding of the previous mentioned classes.\
To implement an AI for your own personal Problem you will have to follow these steps:
1. Design the structure of the NeuralNet using the NeuralNetBuilder
2. implement a NeuralNetFitnessFunction
3. Choose and configure the values of Selector, Recombiner and Mutator
4. Configure the parameters of the Genetic Algorithm
5. combine everything in the GeneticAlgorithm.Builder class

an abstract example could look like this:
```java
        Selector<NeuralNetIndividual> SELECTOR = new EliteSelector<>( 0.1 );
    Recombiner<NeuralNetIndividual> RECOMBINER = new NNUniformCrossoverRecombiner( 2 );
    Mutator<NeuralNetIndividual> MUTATOR = new NNRandomMutator( 0.9, 0.4, new Randomizer( -0.01, 0.01 ), 0.01 );
    int GENS = 50;
    NeuralNetFitnessFunction FITNESS_FUNCTION = (nn) -> //calculate Fitness; 

    NeuralNetSupplier neuralNetSupplier = ( ) -> new NeuralNet.Builder( 100, 2 )
                .addLayer( 50 )
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

# EXAMPLE

This example will show how to use the Framework for a number prediction based on some simple mathematic function.





<a name="license"></a>

# License & copyright

© Tom Lamprecht, David Kupper - FHWS Fakultät Informatik
