package de.fhws.geneticalgorithm.selector;

import de.fhws.geneticalgorithm.Population;
import de.fhws.geneticalgorithm.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TournamentSelector<T extends Individual<T>> extends PercentageSelector<T> {
    private final int tournamentSize;

    public TournamentSelector(double percent, int tournamentSize) {
        super(percent);
        if(tournamentSize < 1)
            throw new IllegalArgumentException("tournamentSize must at least 1");

        this.tournamentSize = tournamentSize;
    }

    @Override
    public void select(Population<T> pop) {
        int goalSize = super.calcGoalSize(pop.getSize());

        if(tournamentSize > pop.getSize())
            throw new RuntimeException("the population size is to small for the tournament size");

        if(pop.getSize() != goalSize) {
            List<Individual<T>> repopulated = repopulate(pop, goalSize);
            pop.replaceAllIndividuals(repopulated);
        }

    }

    private Individual<T> playTournament(Population<T> pop) {
        Random rand = new Random();
        ArrayList<Individual<T>> participants = new ArrayList<>(tournamentSize);

        // get participants
        for(int i = 0; i < tournamentSize; i++) {
            Individual<T> newOne = pop.getIndividuals().get(rand.nextInt(pop.getSize()));
            if(participants.contains(newOne))
                i--;
            else
                participants.add(newOne);
        }

        return Collections.max(participants);
    }

    private List<Individual<T>> repopulate(Population<T> pop, int goalSize) {
        List<Individual<T>> repopulated = new ArrayList<>(goalSize);

        while (repopulated.size() < goalSize) {
            Individual<T> newOne = playTournament(pop);
            repopulated.add(newOne);
        }
        return repopulated;
    }

}
