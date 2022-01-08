package data;

import annualchanges.AnnualChanges;
import annualchanges.ChildUpdate;
import child.Child;
import enums.Category;
import santa.Gift;
import santa.Santa;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that stores all the data from input.
 */
public final class Database {

    private Integer numberOfYears;
    private Santa santa;
    private List<Child> children;
    private List<AnnualChanges> annualChanges;
    private AllYearsChildren allYearsChildren;

    /**
     * Make it singleton
     */
    private static Database database = null;

    /**
     * Singleton function
     */
    public static Database getInstance() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    public Integer getNumberOfYears() {
        return numberOfYears;
    }

    public void setNumberOfYears(final Integer numberOfYears) {
        this.numberOfYears = numberOfYears;
    }

    public Santa getSanta() {
        return santa;
    }

    public void setSanta(final Santa santa) {
        this.santa = santa;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(final List<Child> children) {
        this.children = children;
    }

    public List<AnnualChanges> getAnnualChanges() {
        return annualChanges;
    }

    public void setAnnualChanges(final List<AnnualChanges> annualChanges) {
        this.annualChanges = annualChanges;
    }

    public AllYearsChildren getAllYearsChildren() {
        return allYearsChildren;
    }

    public void setAllYearsChildren(final AllYearsChildren allYearsChildren) {
        this.allYearsChildren = allYearsChildren;
    }

    /**
     * Applies annual changes to database
     */
    public void updateDatabaseByYear(final int i) {
        increaseChildrenAge();
        AnnualChanges annualChange = Database.getInstance()
                .getAnnualChanges().get(i);

        if (annualChange.getNewSantaBudget() != null) {
            Database.getInstance().getSanta()
                    .setSantaBudget(annualChange.getNewSantaBudget());
        }
        for (Gift gift : annualChange.getNewGifts()) {
            Database.getInstance().getSanta().getGiftsList().add(gift);
        }

        for (Child child : annualChange.getNewChildren()) {
            Database.getInstance().getChildren().add(child);
        }
        Collections.sort(Database.getInstance().getChildren());

        for (ChildUpdate childUpdate : annualChange.getChildrenUpdates()) {
            Child child = Utils.getInstance().getChildById(childUpdate.getId());
            if (child != null) {
                if (childUpdate.getNiceScore() != null) {
                    child.getNiceScoreHistory().add(childUpdate.getNiceScore());
                    child.setNiceScore(childUpdate.getNiceScore());
                }
                List<Category> giftPrefs = new ArrayList<>();
                for (Category gift : childUpdate.getGiftsPreferences()) {
                    if (!giftPrefs.contains(gift)) {
                        giftPrefs.add(gift);
                    }
                }
                for (Category gift : child.getGiftsPreferences()) {
                    if (!giftPrefs.contains(gift)) {
                        giftPrefs.add(gift);
                    }
                }
                child.setGiftsPreferences(giftPrefs);
                child.setElf(childUpdate.getElf());
            }
        }
    }
    /**
     * Increases the ages of all children.
     */
    public void increaseChildrenAge() {
        for (Child child : Database.getInstance().getChildren()) {
            child.setAge(child.getAge() + 1);
        }
    }
    /**
     * Saves year for output
     */
    public void saveYear() {
        if (allYearsChildren == null) {
            allYearsChildren = new AllYearsChildren();
        }
        allYearsChildren.addYear();
    }

    /**
     * Completely resets database to prepare for new input file.
     */
    public static void renewDatabase() {
        database = null;
        database = new Database();
    }
}
