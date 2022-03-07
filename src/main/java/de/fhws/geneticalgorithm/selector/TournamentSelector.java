package de.fhws.geneticalgorithm.selector;

import de.fhws.geneticalgorithm.Population;
import de.fhws.geneticalgorithm.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TournamentSelector extends PercentageSelector {
    private int tournamentSize;

    public TournamentSelector(double percent, int tournamentSize) {
        super(percent);
        if(tournamentSize < 1)
            throw new IllegalArgumentException("tournamentSize must at least 1");

        this.tournamentSize = tournamentSize;
    }

    @Override
    public void select(Population<? extends Individual> pop) {
        int goalSize = super.calcGoalSize(pop.getSize());

        if(tournamentSize > pop.getSize())
            throw new RuntimeException("the population size is to small for the tournament size");

        if(pop.getSize() != goalSize) {
            ArrayList<Individual> newIndividuals = new ArrayList<>(goalSize);
            while (newIndividuals.size() < goalSize) {
                newIndividuals.add(playTournament(pop));
            }
        }
    }

    private Individual playTournament(Population<? extends Individual> pop) {
        Random rand = new Random();
        ArrayList<Individual> participants = new ArrayList<>(tournamentSize);

        // get participants
        for(int i = 0; i < tournamentSize; i++) {
            Individual newOne = pop.getIndividuals().get(rand.nextInt(pop.getSize()));
            if(participants.contains(newOne))
                i--;
            else
                participants.add(newOne);
        }

        return Collections.max(participants);
    }
}
