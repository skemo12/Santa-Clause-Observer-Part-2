package child;

import common.Constants;
import enums.ChildType;

public final class ChildFactory {

    private ChildFactory() {
        //constructor for checkstyle
    }
    /**
     * return enum ChildType for Child class
     */
    public static ChildType getChildType(final Child child) {
        if (child.getAge() < Constants.BABY) {
            return ChildType.BABY;
        } else if (child.getAge() < Constants.KID) {
            return ChildType.KID;
        } else if (child.getAge() <= Constants.TEEN) {
            return ChildType.TEEN;
        }
        return ChildType.ADULT;
    }

    /**
     * Child factory
     */
    public static Child createChild(final Child child) {
        return switch (getChildType(child)) {
            case BABY -> new Baby(child);
            case KID -> new Kid(child);
            case TEEN -> new Teen(child);
            case ADULT -> null;
        };

    }

}
