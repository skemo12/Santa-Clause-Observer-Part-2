package strategies;

import child.Child;
import data.Database;
import interfaces.StrategyInterface;
import santa.Santa;

import java.util.Collections;
import java.util.List;

public class IdStrategy implements StrategyInterface {

    /**
     * Strategy method for id strategy
     */
    @Override
    public void applyStrategy() {
        List<Child> children = Database.getInstance().getChildren();
        Santa santa = Database.getInstance().getSanta();
        Collections.sort(children);
        santa.updateBudgetUnit();
        for (Child child : children) {
            santa.giveGiftsToChild(child);
        }
    }
}
