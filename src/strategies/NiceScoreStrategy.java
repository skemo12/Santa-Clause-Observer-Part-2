package strategies;

import child.Child;
import data.Database;
import interfaces.StrategyInterface;
import santa.Santa;

import java.util.Collections;
import java.util.List;

public class NiceScoreStrategy implements StrategyInterface {

    /**
     * Strategy method for id strategy
     */
    @Override
    public void applyStrategy() {
        List<Child> children = Database.getInstance().getChildren();
        Santa santa = Database.getInstance().getSanta();

        santa.updateBudgetUnit();
        children.sort((o1, o2) -> Integer.compare(Double
                .compare(o2.getAverageScore(), o1.getAverageScore()), 0));
        for (Child child : children) {
            santa.giveGiftsToChild(child);
        }
        Collections.sort(children);

    }
}
