# 🏗 How to Model

In order to find a solution for your problem, the framework will need you to provide a fitting model for it.

It's advisable to not overengineer your model though. Really think about what details your model needs, and don't implement any properties that will not be used later on anyway.

## Fitness values

The whole training process is based on the function that provides the fitness values of the models to the framework. This function is basically the heart of the model.\
\
The interface specifying the structure of this function looks like this:

```java
@FunctionalInterface
public interface NeuralNetFitnessFunction {
    double calculateFitness( NeuralNet neuralNet );
}
```

So whatever you want the framework to solve, you will need to be able to tell the framework how good a given model is based on the wanted outcome.\
The basic rule here is: The higher the Fitness Value, the closer is the model to the required result.

A Fitness Function that is used to train the framework to solve the function

$$
f(x) = 2x
$$

May look like this:

```java
NeuralNetFitnessFunction fitnessFunction = (nn) -> {
    double input = Math.random() * 100; //generate a random input to test our model
    double output = nn.calcOutput(new Vector( input )).get(0); //get the result from our model
    double expectedValue = 2 * input; //calculate the real result
    double diff = Math.abs(output - expectedValue);
    return diff == 0 ? Double.MAX_VALUE : 1 / diff; //return a value based on how precise the result from out model was
};
```

{% hint style="info" %}
On the Github Repository you will find a complete implementation for this Example in the directory "Example"
{% endhint %}
