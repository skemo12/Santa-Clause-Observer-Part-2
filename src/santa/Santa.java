package santa;

import child.Child;
import child.ChildFactory;
import child.Baby;
import child.Kid;
import child.Teen;
import common.Constants;
import data.Database;
import enums.Category;
import interfaces.SantaVisitorInterface;
import utils.Utils;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;


/**
 * Class that stores the data of Santa, implements SantaVisitorInterface
 * because Santa is the visitor of every Child.
 */
public final class Santa implements SantaVisitorInterface {
    /**
     * Class for Santa
     * Uses visitor pattern
     * Is the visitor
     */
    private Double santaBudget;
    private List<Gift> giftsList;
    private Double budgetUnit;

    public Santa(final Double santaBudget, final List<Gift> giftsList) {
        this.santaBudget = santaBudget;
        this.giftsList = giftsList;
    }

    public Double getSantaBudget() {
        return santaBudget;
    }

    public void setSantaBudget(final Double santaBudget) {
        this.santaBudget = santaBudget;
    }


    public List<Gift> getGiftsList() {
        return giftsList;
    }

    public void setGiftsList(final List<Gift> giftsList) {
        this.giftsList = giftsList;
    }

    public Double getBudgetUnit() {
        return budgetUnit;
    }

    public void setBudgetUnit(final Double budgetUnit) {
        this.budgetUnit = budgetUnit;
    }

    /**
     * Updates Santa's budgetUnit
     */
    public void updateBudgetUnit() {
        double sum = 0;
        final List<Child> children = Database.getInstance().getChildren();
        for (final Child child : children) {
            sum += child.getAverageScore();
        }
        budgetUnit = santaBudget / sum;
    }
    /**
     * Give child this gifts
     */
    public void giveGiftsToChild(final Child child) {

        double budgetChild = budgetUnit * child.getAverageScore();
        child.setAssignedBudget(budgetChild);
        child.elfBudgets();
        budgetChild = child.getAssignedBudget();
        double currBudget = 0.0;
        for (final Category giftPreferences : child.getGiftsPreferences()) {
            if (Double.compare(currBudget, budgetChild) == 0) {
                break;
            }
            List<Gift> giftsPerCategory = searchGiftsForChild(giftPreferences,
                    currBudget, budgetChild);

            // Adding lowest priced gift that is in stock to child's
            // received gifts
            if (!giftsPerCategory.isEmpty()) {
                Collections.sort(giftsPerCategory);
                for (final Gift gift : giftsPerCategory) {
                    if (gift.getQuantity() != 0) {
                        child.getReceivedGifts().add(gift);
                        currBudget += gift.getPrice();
                        gift.setQuantity(gift.getQuantity() - 1);
                        break;
                    }
                }
            }
        }
        child.elfYellow();
    }
    /**
     * Returns Gifts list containing possible gifts for child
     */
    private List<Gift> searchGiftsForChild(final Category giftPreferences,
                                final double currBudget,
                                final double budgetChild) {

        final List<Gift> giftsPerCategory = new ArrayList<>();
        for (final Gift gift : giftsList) {
            if (gift.getCategory() == giftPreferences) {
                final double auxBudget = gift.getPrice() + currBudget;
                if (Double.compare(auxBudget, budgetChild) < 0) {
                    giftsPerCategory.add(gift);
                }
            }
        }
        return giftsPerCategory;
    }
    /**
     * Calculates the score for every child by applying the visitor method.
     */
    public void calculateScores() {

        final List<Child> updatedChildren = filterChildByAge();

        for (Child child : updatedChildren) {
            child.accept(this);
        }

        for (final Child updatedChild : updatedChildren) {
            final int index = Utils.getIndexOfChild(updatedChild);
            Database.getInstance().getChildren().set(index, updatedChild);
        }

    }

    /**
     * Creates new list of children by categories (Baby, Kid, Teen)
     */
    public List<Child> filterChildByAge() {

        final List<Child> updatedChildren = new ArrayList<>();
        final ListIterator<Child> childListIterator = Database.getInstance()
                .getChildren().listIterator();

        while (childListIterator.hasNext()) {
            final Child child = childListIterator.next();
            Child newChild = ChildFactory.createChild(child);
            if (newChild == null) {
                childListIterator.remove();
            } else {
                updatedChildren.add(newChild);
            }
        }
        return updatedChildren;
    }

    @Override
    public void visit(final Baby child) {
        child.setAverageScore(Constants.MAX_GRADE);
    }

    @Override
    public void visit(final Kid child) {
        if (child.getNiceScoreHistory() != null) {
            Double average = 0.0;
            for (final Double score : child.getNiceScoreHistory()) {
                average += score;
            }
            average = average / child.getNiceScoreHistory().size();
            child.setAverageScore(average);
        }
    }

    @Override
    public void visit(final Teen child) {
        if (child.getNiceScoreHistory() != null) {
            double average = 0.0;
            double divider = 0.0;
            for (int i = 0; i < child.getNiceScoreHistory().size(); i++) {
                final Double score = child.getNiceScoreHistory().get(i);
                final int index = i + 1;
                average += score * index;
                divider += index;
            }
            average = average / divider;
            child.setAverageScore(average);
        }
    }
}
