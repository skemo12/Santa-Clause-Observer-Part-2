package yearsimulation;

import child.Child;
import data.Database;
import interfaces.StrategyInterface;
import santa.Santa;

import java.util.Collections;
import java.util.List;

public final class YearSimulation {

    private StrategyInterface strategy;

    public YearSimulation(final StrategyInterface strategy) {
        this.strategy = strategy;
    }

    /**
     * Simulates one year
     * HappyChristmas!
     */
    public void simulateYear() {

        final List<Child> children = Database.getInstance().getChildren();
        final Santa santa = Database.getInstance().getSanta();

        Collections.sort(children);
        santa.calculateScores();
        for (final Child child : children) {
            child.getReceivedGifts().clear();
            child.addScoreBonus();
        }

        this.strategy.applyStrategy();
    }

    public StrategyInterface getStrategy() {
        return strategy;
    }

    public void setStrategy(final StrategyInterface strategy) {
        this.strategy = strategy;
    }
}
