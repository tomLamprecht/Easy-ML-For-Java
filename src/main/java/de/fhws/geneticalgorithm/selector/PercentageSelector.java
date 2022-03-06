package de.fhws.geneticalgorithm.selector;

public abstract class PercentageSelector implements Selector {
    private double percent;

    public PercentageSelector(double percent) {
        setPercent(percent);
    }

    protected int calcGoalSize(int popSize) {
        return (int) Math.ceil(popSize * percent);
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        if(percent < 0 || percent > 1)
            throw new IllegalArgumentException("percent must be between 0 and 1 (inclusive)");

        this.percent = percent;
    }

}
