package strategies;

import child.Child;
import data.Database;
import enums.Cities;
import interfaces.StrategyInterface;
import santa.Santa;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class NiceCityScoreStrategy implements StrategyInterface {

    /**
     * Strategy method for city nice score strategy
     */
    @Override
    public void applyStrategy() {
        Santa santa = Database.getInstance().getSanta();
        Collections.sort(Database.getInstance().getChildren());
        LinkedHashMap<Cities, List<Child>> map = citiesScores();
        santa.updateBudgetUnit();
        for (Map.Entry<Cities, List<Child>> entry : map.entrySet()) {
            List<Child> children = entry.getValue();
            for (Child child : children) {
                santa.giveGiftsToChild(child);
            }
            Collections.sort(Database.getInstance().getChildren());
        }
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
        for (final Map.Entry<Cities, Double> mapEntry : citiesScoreMap
                .entrySet()) {
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


}
