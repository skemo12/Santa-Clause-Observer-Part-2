package santa;

import child.Baby;
import child.Child;
import child.Kid;
import child.Teen;
import data.Database;
import enums.Category;
import interfaces.SantaVisitorInterface;
import utils.Utils;
import common.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        List<Child> children = Database.getInstance().getChildren();
        for (Child child : children) {
            sum += child.getAverageScore();
        }
        budgetUnit = santaBudget / sum;
    }

    /**
     * Gives every child gifts.
     */
    public void giveGifts() {
        //resetGiftListOrders();
        calculateScores();
        List<Child> children = Database.getInstance().getChildren();
        for (Child child : children) {
            child.getReceivedGifts().clear();
        }
        updateBudgetUnit();
        for (Child child : children) {
            child.addScoreBonus();
            double budgetChild = budgetUnit * child.getAverageScore();
            child.setAssignedBudget(budgetChild);
            //child.elfMagic();
            //budgetChild = child.getAssignedBudget();
            double currBudget = 0.0;
            for (Category giftPreferences : child.getGiftsPreferences()) {
                if (Double.compare(currBudget, budgetChild) == 0) {
                    break;
                }
                List<Gift> giftsPerCategory = new ArrayList<>();
                for (Gift gift : giftsList) {
                    if (gift.getCategory() == giftPreferences) {
                        double auxBudget = gift.getPrice() + currBudget;
                        if (Double.compare(auxBudget, budgetChild) < 0) {
                            giftsPerCategory.add(gift);

                            if (Double.compare(currBudget, budgetChild) == 0) {
                                break;
                            }
                        }
                    }
                }
                if (!giftsPerCategory.isEmpty()) {
                    Collections.sort(giftsPerCategory);
                    for (Gift gift : giftsPerCategory) {
                        if (gift.getQuantity() != 0){
                            child.getReceivedGifts().add(gift);
                            currBudget += gift.getPrice();
                            gift.setQuantity(giftsPerCategory.get(0)
                                    .getQuantity() - 1);
                            break;
                        }
                    }
                }
                if (Double.compare(currBudget, budgetChild) == 0) {
                    break;
                }
            }
            //child.elfYellow();
        }
    }

    /**
     * Calculates the score for every child by applying the visitor method.
     */
    public void calculateScores() {

        List<Child> updatedChildren = filterChildByAge();
        ListIterator<Child> updatedChildListIterator = updatedChildren
                .listIterator();

        while (updatedChildListIterator.hasNext()) {
            Child child = updatedChildListIterator.next();
            if (child.getAge() < Constants.BABY) {
                ((Baby) child).accept(this);
            } else if (child.getAge() < Constants.KID) {
                ((Kid) child).accept(this);
            } else if (child.getAge() <= Constants.TEEN) {
                ((Teen) child).accept(this);
            } else {
                updatedChildListIterator.remove();
            }
        }

        for (Child updatedChild : updatedChildren) {
            int index = Utils.getInstance().getIndexOfChild(updatedChild);
            Database.getInstance().getChildren().set(index, updatedChild);
        }

    }

    /**
     * Creates new list of children by categories (Baby, Kid, Teen)
     */
    public List<Child> filterChildByAge() {

        List<Child> updatedChildren = new ArrayList<>();
        ListIterator<Child> childListIterator = Database.getInstance()
                .getChildren().listIterator();

        while (childListIterator.hasNext()) {
            Child child = childListIterator.next();
            if (child.getAge() < Constants.BABY) {
                updatedChildren.add(new Baby(child));
            } else if (child.getAge() < Constants.KID) {
                updatedChildren.add(new Kid(child));
            } else if (child.getAge() <= Constants.TEEN) {
                updatedChildren.add(new Teen(child));
            } else {
                childListIterator.remove();
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
            for (Double score : child.getNiceScoreHistory()) {
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
                Double score = child.getNiceScoreHistory().get(i);
                int index = i + 1;
                average += score * index;
                divider += index;
            }
            average = average / divider;
            child.setAverageScore(average);
        }
    }
}
