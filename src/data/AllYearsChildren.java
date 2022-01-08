package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that stores all changes from all years. Used for output.
 */
public final class AllYearsChildren {
    private List<AnnualChildren> annualChildren;

    public AllYearsChildren() {
        this.setAnnualChildren(new ArrayList<>());
    }

    public List<AnnualChildren> getAnnualChildren() {
        return annualChildren;
    }

    public void setAnnualChildren(final List<AnnualChildren> annualChildren) {
        this.annualChildren = annualChildren;
    }

    /**
     * Adds the result of the year to the output Object.
     */
    public void addYear() {
        getAnnualChildren().add(new AnnualChildren(Database
                .getInstance().getChildren()));
    }
}
