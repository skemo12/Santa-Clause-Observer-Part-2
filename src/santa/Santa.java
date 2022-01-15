package santa;

import child.Baby;
import child.Child;
import child.Kid;
import child.Teen;
import common.Constants;
import data.Database;
import enums.Category;
import enums.Cities;
import enums.CityStrategyEnum;
import interfaces.SantaVisitorInterface;
import utils.Utils;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;


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
     * Gives every child gifts.
     */
    public void giveGifts(final CityStrategyEnum strategyEnum) {
        calculateScores();
        final List<Child> children = Database.getInstance().getChildren();
        for (final Child child : children) {
            child.getReceivedGifts().clear();
            child.addScoreBonus();
        }
        Collections.sort(children);
        updateBudgetUnit();
        if (strategyEnum == CityStrategyEnum.NICE_SCORE) {
            children.sort((o1, o2) -> Integer.compare(Double
                    .compare(o2.getAverageScore(), o1.getAverageScore()), 0));
        }
        if (strategyEnum == CityStrategyEnum.NICE_SCORE_CITY) {
            niceCityScoreStrategy();
            return;
        }

        for (final Child child : children) {
            double budgetChild = budgetUnit * child.getAverageScore();
            child.setAssignedBudget(budgetChild);
            child.elfMagic();
            budgetChild = child.getAssignedBudget();
            double currBudget = 0.0;
            for (final Category giftPreferences : child.getGiftsPreferences()) {
                if (Double.compare(currBudget, budgetChild) == 0) {
                    break;
                }
                final List<Gift> giftsPerCategory = new ArrayList<>();
                for (final Gift gift : giftsList) {
                    if (gift.getCategory() == giftPreferences) {
                        final double auxBudget = gift.getPrice() + currBudget;
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
                    for (final Gift gift : giftsPerCategory) {
                        if (gift.getQuantity() != 0) {
                            child.getReceivedGifts().add(gift);
                            currBudget += gift.getPrice();
                            gift.setQuantity(gift.getQuantity() - 1);
                            break;
                        }
                    }
                }
                if (Double.compare(currBudget, budgetChild) == 0) {
                    break;
                }
            }
            child.elfYellow();
        }
        Collections.sort(Database.getInstance().getChildren());
    }

    /**
     * Calculates the score for every child by applying the visitor method.
     */
    public void calculateScores() {

        final List<Child> updatedChildren = filterChildByAge();
        final ListIterator<Child> updatedChildListIterator = updatedChildren
                .listIterator();

        while (updatedChildListIterator.hasNext()) {
            final Child child = updatedChildListIterator.next();
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

        for (final Child updatedChild : updatedChildren) {
            final int index = Utils.getInstance().getIndexOfChild(updatedChild);
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
    /**
     * Returns sorted cities list by cities average score
     */
    public List<Cities> sortCities() {
        final HashMap<Cities, Double> citiesScoreMap = new HashMap<>();
        final List<Cities> sortedCities = new ArrayList<>();

        for (final Child child : Database.getInstance().getChildren()) {
            if (!citiesScoreMap.containsKey(child.getCity())) {
                citiesScoreMap.put(child.getCity(), 0.0);
                sortedCities.add(child.getCity());
            }
        }
        for (final Map.Entry<Cities, Double> mapEntry : citiesScoreMap.entrySet()) {
            Double average = 0.0;
            Integer divider = 0;
            for (final Child child : Database.getInstance().getChildren()) {
                if (child.getCity() == mapEntry.getKey()) {
                    average += child.getAverageScore();
                    divider++;
                }
            }
            average = average / divider;
            mapEntry.setValue(average);
        }
        sortedCities.sort((o1, o2) -> {
            if (citiesScoreMap.get(o1) > citiesScoreMap.get(o2)) {
                return -1;
            } else if (citiesScoreMap.get(o1) < citiesScoreMap.get(o2)) {
                return 1;
            } else {
                return o1.name().compareToIgnoreCase(o2.name());
            }
        });

        return sortedCities;
    }

    /**
     * Calculate cities scores
     */
    public LinkedHashMap<Cities, List<Child>> citiesScores() {
        final LinkedHashMap<Cities, List<Child>> map = new LinkedHashMap<>();
        final List<Cities> sortedCities = sortCities();

        for (final Cities city : sortedCities) {
            final List<Child> children = new ArrayList<>();
            for (final Child child : Database.getInstance().getChildren()) {
                if (child.getCity() == city) {
                    children.add(child);
                }
            }
            map.put(city, children);
        }

        return map;
    }
    /**
     * Applies niceCityScore strategy
     */
    public void niceCityScoreStrategy() {
        final LinkedHashMap<Cities, List<Child>> map = citiesScores();
        updateBudgetUnit();
        for (final Map.Entry<Cities, List<Child>> entry : map.entrySet()) {
            final List<Child> children = entry.getValue();
            for (final Child child : children) {
                double budgetChild = budgetUnit * child.getAverageScore();
                child.setAssignedBudget(budgetChild);
                child.elfMagic();
                budgetChild = child.getAssignedBudget();
                double currBudget = 0.0;
                for (final Category giftPreferences : child.getGiftsPreferences()) {
                    if (Double.compare(currBudget, budgetChild) == 0) {
                        break;
                    }
                    final List<Gift> giftsPerCategory = new ArrayList<>();
                    for (final Gift gift : giftsList) {
                        if (gift.getCategory() == giftPreferences) {
                            final double auxBudget = gift.getPrice() + currBudget;
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
                        for (final Gift gift : giftsPerCategory) {
                            if (gift.getQuantity() != 0) {
                                child.getReceivedGifts().add(gift);
                                currBudget += gift.getPrice();
                                gift.setQuantity(gift.getQuantity() - 1);
                                break;
                            }
                        }
                    }
                    if (Double.compare(currBudget, budgetChild) == 0) {
                        break;
                    }
                }
                child.elfYellow();
            }
            Collections.sort(Database.getInstance().getChildren());
        }
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
