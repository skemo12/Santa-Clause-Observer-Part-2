package annualchanges;

import child.Child;
import enums.CityStrategyEnum;
import santa.Gift;

import java.util.List;

/**
 * Class that stores all changes from 1 year.
 */
public final class AnnualChanges {

    private Double newSantaBudget;
    private List<Gift> newGifts;
    private List<Child> newChildren;
    private List<ChildUpdate> childrenUpdates;
    private CityStrategyEnum strategy;

    public AnnualChanges(final Double newSantaBudget, final List<Gift> newGifts,
                         final List<Child> newChildren,
                         final List<ChildUpdate> childrenUpdates,
                         final CityStrategyEnum strategy) {
        this.setNewSantaBudget(newSantaBudget);
        this.setNewGifts(newGifts);
        this.setNewChildren(newChildren);
        this.setChildrenUpdates(childrenUpdates);
        this.setStrategy(strategy);
    }

    public Double getNewSantaBudget() {
        return newSantaBudget;
    }

    public void setNewSantaBudget(final Double newSantaBudget) {
        this.newSantaBudget = newSantaBudget;
    }

    public List<Gift> getNewGifts() {
        return newGifts;
    }

    public void setNewGifts(final List<Gift> newGifts) {
        this.newGifts = newGifts;
    }

    public List<Child> getNewChildren() {
        return newChildren;
    }

    public void setNewChildren(final List<Child> newChildren) {
        this.newChildren = newChildren;
    }

    public List<ChildUpdate> getChildrenUpdates() {
        return childrenUpdates;
    }

    public void setChildrenUpdates(final List<ChildUpdate> childrenUpdates) {
        this.childrenUpdates = childrenUpdates;
    }

    public CityStrategyEnum getStrategy() {
        return strategy;
    }

    public void setStrategy(final CityStrategyEnum strategy) {
        this.strategy = strategy;
    }
}
