package child;

import com.fasterxml.jackson.annotation.JsonIgnore;
import common.Constants;
import data.Database;
import enums.Category;
import enums.Cities;
import enums.ElvesType;
import interfaces.SantaVisitorInterface;
import interfaces.SantaVisitable;
import santa.Gift;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that stores the data of a child, implements comparable for sorting by
 * id.
 */
public class Child implements Comparable<Child>, SantaVisitable {
    private Integer id;
    private String lastName;
    private String firstName;
    private Cities city;
    private Integer age;
    private List<Category> giftsPreferences;
    @JsonIgnore
    private Double niceScore;
    private Double averageScore;
    private List<Double> niceScoreHistory;
    private Double assignedBudget;
    private List<Gift> receivedGifts;
    @JsonIgnore
    private ElvesType elf;
    @JsonIgnore
    private Double niceScoreBonus;

    /**
     * Adds niceScoreBonus to child averageScore
     */
    public void addScoreBonus() {
        if (Double.compare(this.getNiceScoreBonus(), 0) == 0) {
            return;
        }
        final double newAverageScore = this.getAverageScore() * this
                .getNiceScoreBonus() / 100;
        this.setAverageScore(this.getAverageScore() + newAverageScore);
        if (this.getAverageScore() > Constants.MAX_GRADE) {
            this.setAverageScore(Constants.MAX_GRADE);
        }
    }
    /**
     * Applies elf for BLACK and PINK elves
     */
    public void elfBudgets() {
        if (elf == ElvesType.BLACK) {
            assignedBudget = assignedBudget - assignedBudget * Constants
                    .ELF_MULTIPLIER / Constants.ELF_DIVIDER;
        }
        if (elf == ElvesType.PINK) {
            assignedBudget = assignedBudget + assignedBudget * Constants
                    .ELF_MULTIPLIER / Constants.ELF_DIVIDER;
        }
    }

    /**
     * Gives gift according to yellow elf.
     */
    public void elfYellow() {
        if (elf == ElvesType.YELLOW) {
            if (receivedGifts.isEmpty()) {
                final Category favoriteCategory = giftsPreferences.get(0);
                Gift lowestPricedGift = null;
                for (final Gift gift : Database.getInstance()
                        .getSanta().getGiftsList()) {
                    if (gift.getCategory() == favoriteCategory) {
                        if (lowestPricedGift == null) {
                            lowestPricedGift = gift;
                        } else if (Double.
                                compare(lowestPricedGift.getPrice(),
                                        gift.getPrice()) > 0) {
                            lowestPricedGift = gift;
                        }

                    }
                }
                if (lowestPricedGift != null
                        && !lowestPricedGift.getQuantity().equals(0)) {
                    lowestPricedGift.setQuantity(
                            lowestPricedGift.getQuantity() - 1);
                    receivedGifts.add(lowestPricedGift);
                }
            }
        }
    }

    public static final class Builder {
        private Integer id;
        private String lastName;
        private String firstName;
        private Cities city;
        private Integer age;
        private List<Category> giftsPreferences;
        private Double niceScore;
        private List<Double> niceScoreHistory;
        private List<Gift> receivedGifts;
        private ElvesType elf;
        private Double niceScoreBonus;

        public Builder(final Integer id, final String lastName, final String firstName,
                       final Cities city, final Integer age,
                       final List<Category> giftsPreferences, final Double niceScore) {
            this.id = id;
            this.lastName = lastName;
            this.firstName = firstName;
            this.city = city;
            this.age = age;
            this.giftsPreferences = giftsPreferences;
            this.niceScore = niceScore;
            this.niceScoreHistory = new ArrayList<>();
            this.niceScoreHistory.add(niceScore);
            this.receivedGifts = new ArrayList<>();
        }
        /**
         * Sets builder niceScoreBonus
         */
        public Builder niceScoreBonus(final Double niceScoreBonusSet) {
            this.niceScoreBonus = niceScoreBonusSet;
            return this;
        }
        /**
         * Sets builder elf
         */
        public Builder elfType(final ElvesType elfSet) {
            this.elf = elfSet;
            return this;
        }

        /**
         * build method for Builder
         */
        public Child build() {
            return new Child(this);
        }
    }

    private Child(final Builder builder) {
        this.id = builder.id;
        this.lastName = builder.lastName;
        this.firstName = builder.firstName;
        this.city = builder.city;
        this.age = builder.age;
        this.giftsPreferences = builder.giftsPreferences;
        this.niceScore = builder.niceScore;
        this.niceScoreHistory = builder.niceScoreHistory;
        this.receivedGifts = builder.receivedGifts;
        this.elf = builder.elf;
        this.niceScoreBonus = builder.niceScoreBonus;

    }
    public Child(final Child child) {
        this.setId(child.getId());
        this.setAge(child.getAge());
        this.setNiceScore(child.getNiceScore());
        this.setFirstName(child.getFirstName());
        this.setLastName(child.getLastName());
        this.setCity(child.getCity());
        this.setGiftsPreferences(new ArrayList<>(child.getGiftsPreferences()));
        this.setReceivedGifts(new ArrayList<>(child.getReceivedGifts()));
        this.setAverageScore(child.getAverageScore());
        this.setAssignedBudget(child.getAssignedBudget());
        this.setNiceScoreHistory(new ArrayList<>(child.getNiceScoreHistory()));
        this.setElf(child.getElf());
        this.setNiceScoreBonus(child.getNiceScoreBonus());
    }

    /**
     * Getter for id
     */
    public final Integer getId() {
        return id;
    }
    /**
     * Setter for id
     */
    public final void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Getter for lastName
     */
    public final String getLastName() {
        return lastName;
    }
    /**
     * Setter for lastName
     */
    public final void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    /**
     * Getter for firstName
     */
    public final String getFirstName() {
        return firstName;
    }
    /**
     * Setter for firstName
     */
    public final void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    /**
     * Getter for city
     */
    public final Cities getCity() {
        return city;
    }
    /**
     * Setter for city
     */
    public final void setCity(final Cities city) {
        this.city = city;
    }
    /**
     * Getter for age
     */
    public final Integer getAge() {
        return age;
    }
    /**
     * Setter for age
     */
    public final void setAge(final Integer age) {
        this.age = age;
    }
    /**
     * Getter for giftsPreferences
     */
    public final List<Category> getGiftsPreferences() {
        return giftsPreferences;
    }
    /**
     * Setter for giftsPreferences
     */
    public final void setGiftsPreferences(final List<Category> giftsPreferences) {
        this.giftsPreferences = giftsPreferences;
    }
    /**
     * Getter for niceScore
     */
    public final Double getNiceScore() {
        return niceScore;
    }
    /**
     * Setter for niceScore
     */
    public final void setNiceScore(final Double niceScore) {
        this.niceScore = niceScore;
    }
    /**
     * Getter for averageScore
     */
    public final Double getAverageScore() {
        return averageScore;
    }
    /**
     * Setter for averageScore
     */
    public final void setAverageScore(final Double averageScore) {
        this.averageScore = averageScore;
    }
    /**
     * Getter for niceScoreHistory
     */
    public final List<Double> getNiceScoreHistory() {
        return niceScoreHistory;
    }
    /**
     * Setter for niceScoreHistory
     */
    public final void setNiceScoreHistory(final List<Double> niceScoreHistory) {
        this.niceScoreHistory = niceScoreHistory;
    }
    /**
     * Getter for assignedBudget
     */
    public final Double getAssignedBudget() {
        return assignedBudget;
    }
    /**
     * Setter for assignedBudget
     */
    public final void setAssignedBudget(final Double assignedBudget) {

        this.assignedBudget = assignedBudget;
    }
    /**
     * Getter for receivedGifts
     */
    public final List<Gift> getReceivedGifts() {
        return receivedGifts;
    }
    /**
     * Setter for receivedGifts
     */
    public final void setReceivedGifts(final List<Gift> receivedGifts) {
        this.receivedGifts = receivedGifts;
    }
    /**
     * Getter for elf
     */
    public final ElvesType getElf() {
        return elf;
    }
    /**
     * Setter for elf
     */
    public final void setElf(final ElvesType elf) {
        this.elf = elf;
    }
    /**
     * Getter for niceScoreBonus
     */
    public final Double getNiceScoreBonus() {
        return niceScoreBonus;
    }
    /**
     * Setter for niceScoreBonus
     */
    public final void setNiceScoreBonus(final Double niceScoreBonus) {
        this.niceScoreBonus = niceScoreBonus;
    }

    /**
     * compareTo method from Comparable interface in order to sort Child list
     * by id.
     */
    @Override
    public int compareTo(final Child o) {
        return getId() - o.getId();
    }

    /**
     * Method for visitable interface
     */
    @Override
    public void accept(final SantaVisitorInterface v) {

    }
}
